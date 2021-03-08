package com.alexsanderfranco.gistapp.detail.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.databinding.ActivityDetailBinding
import com.alexsanderfranco.gistapp.detail.viewmodel.DetailViewModel
import com.alexsanderfranco.gistapp.list.view.GistFileListAdapter
import com.alexsanderfranco.gistapp.shared.utils.UiUtils
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class DetailActivity : AppCompatActivity() {
    companion object {
        const val GIST_PARCELABLE = "gist"
    }

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()
    private val gistFileListAdapter: GistFileListAdapter by inject(GistFileListAdapter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setupGist()
        setUpObservers()
    }

    private fun getGist() = intent.extras?.getParcelable<Gist>(GIST_PARCELABLE)

    private fun setupGist() {
        val gistFromIntent = getGist()
        gistFromIntent?.let {
            detailViewModel.setGist(gistFromIntent)
        }
    }

    private fun setUpObservers() {
        detailViewModel.gist.observe(this, { gist ->
            setupUi(gist)
        })
    }

    private fun setupUi(gist: Gist) {
        gist.apply {
            owner?.let { owner ->
                Picasso.get().load(owner.avatarUrl).into(binding.gistOwnerPhotoIv)
                binding.gistOwnerNameTv.text = owner.login
            }
            binding.gistDateTv.text = createdAt

            val linearLayoutManager = LinearLayoutManager(this@DetailActivity)
            binding.gistFileListRv.apply {
                layoutManager = linearLayoutManager
                adapter = gistFileListAdapter
            }
            files?.let { gistFileListAdapter.update(it) }

            val favoriteImageId = UiUtils.getFavoriteImageId(gist.isFavorite)
            val favoriteImage = ContextCompat.getDrawable(
                    this@DetailActivity,
                    favoriteImageId
            )
            binding.gistFavoriteIb.setImageDrawable(favoriteImage)
            binding.gistFavoriteIb.tag = favoriteImageId
            binding.gistFavoriteIb.setOnClickListener {
                detailViewModel.onClickFavorite(
                        gist = gist
                )
            }
        }
    }
}