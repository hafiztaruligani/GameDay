package com.hafiztaruligani.gamesday.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.gamesday.R
import com.hafiztaruligani.gamesday.databinding.ItemGameBinding
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.util.Cons.GAME_IMAGE_CORNER_RADIUS
import com.hafiztaruligani.gamesday.util.GamesDiffUtil
import com.hafiztaruligani.gamesday.util.glide

class GamesPagingAdapter(
    diffUtil: GamesDiffUtil,
    private val navigate: (game: GameSimple, view: View) -> Unit
) : PagingDataAdapter<GameSimple, GamesPagingAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(val binding: ItemGameBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)

        data?.let {
            holder.binding.apply {

                // bind data
                name.text = data.name
                releaseDate.text = data.releaseDate
                rating.text = data.rating

                // bind image
                image.glide(
                    resource = data.image,
                    topLeft = GAME_IMAGE_CORNER_RADIUS,
                    topRight = GAME_IMAGE_CORNER_RADIUS,
                    bottomLeft = GAME_IMAGE_CORNER_RADIUS,
                    bottomRight = GAME_IMAGE_CORNER_RADIUS,
                    centerCrop = true,
                    placeholder = R.drawable.rounded_background
                )

                ViewCompat.setTransitionName(image, data.id.toString())

                // on click -> navigate into detail fragment
                root.setOnClickListener {
                    navigate.invoke(data, image)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}
