package com.hafiztaruligani.gamesday.presentation.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiztaruligani.gamesday.databinding.FragmentFavouriteBinding
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.presentation.adapters.FavoriteGamesAdapter
import com.hafiztaruligani.gamesday.presentation.main.MainActivity
import com.hafiztaruligani.gamesday.util.GamesDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private val viewModel by viewModels<FavouriteViewModel>()
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var adapter: FavoriteGamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        setupRc()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun bindData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                // ... if state error only (data is not null), then fragment just showing error message
                // ... but if state error and data is null, then pop back stack
                if(state.error.isNotBlank()){
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    if (state.data == null) findNavController().popBackStack()
                }

                // loading visibility depend on (state -> loading)
                (activity as MainActivity).loading(state.loading)

                // submit data into adapter
                // if the list is empty, then "empty favorite list" visible
                state.data?.let {
                    adapter.asyncList.submitList(it)
                    binding.empty.isVisible = it.isEmpty()
                }

            }
        }
    }

    // setup adapter and RC
    private fun setupRc() {
        adapter = FavoriteGamesAdapter(GamesDiffUtil(), ::navigateIntoDetail)
        binding.gamesRc.adapter = adapter
        binding.gamesRc.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    // this function used for adapter to navigate to fragment
    private fun navigateIntoDetail(gameSimple: GameSimple, view: View) {

        // initiate transition destination
        val transitionName = "${gameSimple.id}${gameSimple.name}"
        val extras = FragmentNavigatorExtras(view to transitionName)

        // direction ito detail fragment
        val directions = FavouriteFragmentDirections.actionFavouriteFragmentToDetailFragment(gameSimple, transitionName)
        findNavController().navigate(
            directions = directions,
            navigatorExtras = extras
        )

    }


}