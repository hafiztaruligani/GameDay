package com.hafiztaruligani.gamesday.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.gamesday.databinding.ItemGameBinding
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.util.Cons
import com.hafiztaruligani.gamesday.util.GamesDiffUtil
import com.hafiztaruligani.gamesday.util.glide

class FavoriteGamesAdapter(
    diffUtil: GamesDiffUtil,
    private val detail:(game: GameSimple, view: View) -> Unit
) : RecyclerView.Adapter<FavoriteGamesAdapter.ViewHolder>() {

    val asyncList = AsyncListDiffer(this, diffUtil)

    inner class ViewHolder(val binding: ItemGameBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder( ItemGameBinding.inflate( LayoutInflater.from(parent.context), parent, false ) )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = asyncList.currentList[position]
        holder.binding.apply {

            name.text = data.name
            releaseDate.text = data.releaseDate
            rating.text = data.rating

            // bind image
            image.glide(
                resource = data.image,
                topLeft = Cons.GAME_IMAGE_CORNER_RADIUS,
                topRight = Cons.GAME_IMAGE_CORNER_RADIUS,
                bottomLeft = Cons.GAME_IMAGE_CORNER_RADIUS,
                bottomRight = Cons.GAME_IMAGE_CORNER_RADIUS,
                centerCrop = true
            )

            ViewCompat.setTransitionName(image, data.name)
            root.setOnClickListener {
                detail.invoke(data, image)
            }

        }
    }

    override fun getItemCount(): Int = asyncList.currentList.size

}
