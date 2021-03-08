package com.alexsanderfranco.gistapp.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexsanderfranco.gistapp.R
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.shared.utils.UiUtils
import com.squareup.picasso.Picasso
import org.koin.java.KoinJavaComponent.inject

class GistListAdapter : RecyclerView.Adapter<GistListAdapter.GistViewHolder>() {
    private lateinit var onClickItem: (Gist) -> Unit
    private lateinit var onClickFavorite: (Gist) -> Unit
    private val items: MutableList<Gist> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GistViewHolder(
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gist_item, parent, false),
        onClickItem = onClickItem,
        onClickFavorite = onClickFavorite
    )

    override fun onBindViewHolder(holder: GistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun init(onClickItem: (Gist) -> Unit, onClickFavorite: (Gist) -> Unit) {
        this.onClickItem = onClickItem
        this.onClickFavorite = onClickFavorite
    }

    fun update(gists: List<Gist>) {
        items.clear()
        items.addAll(gists)
        notifyDataSetChanged()
    }

    class GistViewHolder(
        private val view: View,
        private val onClickItem: (Gist) -> Unit,
        private val onClickFavorite: (Gist) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val gistFileListAdapter: GistFileListAdapter by inject(GistFileListAdapter::class.java)

        private val ownerPhotoImageView: ImageView =
            view.findViewById(R.id.gist_item_owner_photo_iv)
        private val ownerNameTextView: TextView =
            view.findViewById(R.id.gist_item_owner_name_tv)
        private val dateTextView: TextView = view.findViewById(R.id.gist_item_date_tv)
        private val favoriteImageButton: ImageButton =
            view.findViewById(R.id.gist_item_favorite_ib)
        private val filesRecyclerView: RecyclerView =
            view.findViewById(R.id.gist_file_list_rv)


        fun bind(gist: Gist) {
            gist.apply {
                view.setOnClickListener { onClickItem(gist) }
                view.tag = gist.id
                owner?.let { owner ->
                    Picasso.get().load(owner.avatarUrl).into(ownerPhotoImageView)
                    ownerNameTextView.text = owner.login
                }
                dateTextView.text = createdAt

                val linearLayoutManager = LinearLayoutManager(view.context)
                filesRecyclerView.apply {
                    layoutManager = linearLayoutManager
                    adapter = gistFileListAdapter
                }
                files?.let { gistFileListAdapter.update(it) }

                val favoriteImageId = UiUtils.getFavoriteImageId(gist.isFavorite)
                val favoriteImage = ContextCompat.getDrawable(view.context, favoriteImageId)
                favoriteImageButton.setImageDrawable(favoriteImage)
                favoriteImageButton.tag = favoriteImageId
                favoriteImageButton.setOnClickListener { onClickFavorite(gist) }
            }
        }
    }

}