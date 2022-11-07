package com.hafiztaruligani.gamesday.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.gamesday.databinding.FragmentHomeBinding
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.presentation.adapters.GamesPagingAdapter
import com.hafiztaruligani.gamesday.presentation.adapters.PagingLoadStateAdapter
import com.hafiztaruligani.gamesday.presentation.main.MainActivity
import com.hafiztaruligani.gamesday.util.GamesDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: GamesPagingAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()

        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRc()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupRc() {

        // setup RC
        val diffUtil = GamesDiffUtil()
        adapter = GamesPagingAdapter(diffUtil, ::navigateIntoDetail)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val loadStateAdapter = PagingLoadStateAdapter(adapter::retry)
        binding.apply {
            gamesRc.adapter = adapter.withLoadStateFooter(loadStateAdapter)
            gamesRc.layoutManager = layoutManager
        }

        setupUpwardButton()

        var initialize = false
        adapter.addOnPagesUpdatedListener {
            if(initialize)
                layoutManager.scrollToPosition(0)
        }

        lifecycleScope.launchWhenCreated {

            // collecting load state for load state adapter and (RC, error and message visibility)
            adapter.loadStateFlow.collectLatest { loadState ->

                // in here data is loaded -> dismiss splash screen
                dataLoaded()

                // prevent rc scrolling to bottom when first load (REFRESH)
                if(loadState.mediator?.refresh is LoadState.Loading)
                    initialize = true // layoutManager.scrollToPosition(0)

                loadStateAdapter.loadState = loadState.mediator?.append ?: loadState.refresh

                // add refresh error listener
                binding.apply {
                    val error = (loadState.mediator?.refresh is LoadState.Error) && adapter.itemCount < 1
                    gamesRc.isVisible = !error
                    message.isVisible = error
                    retry.isVisible = error
                }

            }
        }

    }

    private fun setupUpwardButton() {
        val btnUpward = binding.btnUpward

        binding.gamesRc.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if( layoutManager.findFirstVisibleItemPosition() == 0 )
                    btnUpward.hide()
                else btnUpward.show()
            }
        })



        btnUpward.setOnClickListener{
            binding.gamesRc.smoothScrollToPosition(0)
        }

    }

    private fun bindData() {
        lifecycleScope.launch {

            // delete button visible if query not empty
            launch {
                viewModel.isQueryNotEmpty.collect{
                    binding.btnDelete.isVisible = it
                }
            }

            // submit games data into adapter
            viewModel.games.collectLatest {
                adapter.submitData(it)
            }

        }
    }

    private fun navigateIntoDetail(game: GameSimple, view: View){

        // initiate transition destination
        val transitionName = (game.id+10).toString()
        val extras = FragmentNavigatorExtras(view to transitionName)

        // direction ito detail fragment
        val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(game, transitionName)
        findNavController().navigate(
            directions = directions,
            navigatorExtras = extras
        )

    }

    // this function used to notify main activity that data is loaded -> dismiss splashscreen
    private fun dataLoaded(){
        (activity as MainActivity).dismissSplashScreen()
    }

}