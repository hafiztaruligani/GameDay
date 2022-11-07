package com.hafiztaruligani.gamesday.presentation.detail

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hafiztaruligani.gamesday.databinding.FragmentDetailBinding
import com.hafiztaruligani.gamesday.presentation.main.MainActivity
import com.hafiztaruligani.gamesday.util.glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var binding: FragmentDetailBinding
    private val args : DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)

        setupTransition()

        // submit the game id into viewModel
        viewModel.submitId(args.game.id)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {
        lifecycleScope.launchWhenCreated {

            // collecting ui state from viewModel
            viewModel.uiState.collect { state ->

                // ... if state error only (data is not null), then fragment just showing error message
                // ... but if state error and data is null, then pop back stack
                if(state.error.isNotBlank()){
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                    if (state.data == null) findNavController().popBackStack()
                }

                binding.apply {

                    // loading state
                    (activity as MainActivity).loading(state.loading)

                    state.data?.let { data ->

                        name.text = data.name
                        releaseDate.text = data.releaseDate
                        rating.text = data.rating
                        company.text = data.company
                        playCount.text = data.playCount

                        // formatting description text that have html format
                        launch {
                            val desc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(data.description, Html.FROM_HTML_MODE_LEGACY)
                            } else {
                                @Suppress("DEPRECATION")
                                Html.fromHtml(data.description)
                            }
                            description.text = desc
                        }

                        // notify main activity to set favorite button (on/off)
                        (activity as MainActivity).setFavorite(data.favorite)

                    }

                }
            }
        }
    }

    private fun setupTransition() {

        // set transition destination
        ViewCompat.setTransitionName(binding.image, args.transitionName)

        // set transition animation
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        animation.duration = 250
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        binding.apply {
            val data = args.game

            // set image
            binding.image.glide(data.image, override = false, centerCrop = true)

            // set image height
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            binding.image.layoutParams.height = (screenHeight*0.4).toInt()
            name.text = data.name
            releaseDate.text = data.releaseDate
            rating.text = data.rating

        }

    }

    // this function used to receive onClickFavorite button event from main activity
    // pass event to viewModel to response the event
    fun onClickFavorite() {
        viewModel.onClickFavorite()
    }

}