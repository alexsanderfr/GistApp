package com.alexsanderfranco.gistapp.list.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexsanderfranco.gistapp.R
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.databinding.ActivityGistListBinding
import com.alexsanderfranco.gistapp.detail.view.DetailActivity
import com.alexsanderfranco.gistapp.list.viewmodel.GistListViewModel
import com.alexsanderfranco.gistapp.shared.enum.VisibilityStatus
import com.alexsanderfranco.gistapp.shared.utils.NetworkUtils
import com.alexsanderfranco.gistapp.shared.utils.UiUtils
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject


class GistListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGistListBinding
    private val gistListAdapter: GistListAdapter by inject(GistListAdapter::class.java)
    private val listViewModel: GistListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup root view
        super.onCreate(savedInstanceState)
        binding = ActivityGistListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupUi()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        setupShowFavoritesSwitch(menu)
        return true
    }


    /** Responsible for controlling if favorites or latest gists are shown. */
    private fun setupShowFavoritesSwitch(menu: Menu) {
        val showFavoritesMenu = menu.findItem(R.id.main_menu_show_favorites)
        val showFavoritesMenuView = showFavoritesMenu.actionView
        val showFavoritesSwitch = showFavoritesMenuView
            .findViewById<SwitchMaterial>(R.id.show_favorites_sw)

        // Disables switching from favorites when connection is not available
        NetworkUtils.registerNetworkCallback(
            activity = this@GistListActivity,
            onNetworkAvailable = {
                showFavoritesSwitch.isEnabled = true
            },
            onNetworkUnavailable = {
                UiUtils.showSnackBar(
                    binding.root,
                    getString(R.string.no_internet_message)
                )
                showFavoritesSwitch.isChecked = true
                showFavoritesSwitch.isEnabled = false
                listViewModel.setShowFavorites(true)
            }
        )

        showFavoritesSwitch.setOnCheckedChangeListener { _, isChecked ->
            listViewModel.setShowFavorites(isChecked)
        }
    }

    /** Hooks up with viewModel to setup recycler view. */
    private fun setupObservers() {
        // Setup show favorites
        listViewModel.showFavorites.observe(this, { showFavorites ->
            val isConnected = NetworkUtils.isConnected(this@GistListActivity)
            if (showFavorites || !isConnected) {
                listViewModel.fetchFavorites()
            } else {
                listViewModel.fetchGistList()
            }
        })

        // Setup gist list
        listViewModel.gistList.observe(this, { gists ->
            if (gists != null) {
                if (gists.isEmpty()) {
                    setVisibility(status = VisibilityStatus.NO_CONTENT)
                } else {
                    gists.let {
                        gistListAdapter.update(it)
                    }
                }
            }
        })

        // Setup progress bar
        listViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                setVisibility(status = VisibilityStatus.LOADING)
            } else {
                setVisibility(status = VisibilityStatus.CONTENT)
            }
        })
    }

    private fun setupUi() {
        // Setup adapter
        gistListAdapter.init(
            onClickItem = { gist -> goToDetail(gist) },
            onClickFavorite = { gist -> listViewModel.onClickFavorite(gist) }
        )

        // Setup recycler view
        val linearLayoutManager = LinearLayoutManager(this@GistListActivity)
        binding.gistsRv.apply {
            isNestedScrollingEnabled = false
            layoutManager = linearLayoutManager
            adapter = gistListAdapter
        }

        // Setup endless scrolling
        binding.gistsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        listViewModel.loadMore()
                    }
                }
            }
        })


        // Setup FAB
        binding.searchFab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_search, null)
            val editText = view.findViewById<EditText>(R.id.search_et)
            editText.setText(listViewModel.getQuery())
            builder.setView(view)
                .setPositiveButton(R.string.search_action)
                { dialog, _ ->
                    val query = editText.text.toString()
                    listViewModel.setQuery(query)
                    UiUtils.showPermanentSnackBar(
                        binding.root,
                        getString(R.string.search_snack_bar).format(query),
                        getString(R.string.clear_action)
                    ) {
                        listViewModel.setQuery("")
                    }
                    dialog.cancel()
                }
                .setNeutralButton(R.string.cancel_action)
                { dialog, _ ->
                    dialog.cancel()
                }.setNegativeButton(R.string.clear_action)
                { dialog, _ ->
                    listViewModel.setQuery(null)
                    dialog.cancel()
                }
            builder.create().show()
        }
    }

    private fun setVisibility(status: VisibilityStatus) {
        when (status) {
            VisibilityStatus.LOADING -> {
                binding.gistsPb.visibility = View.VISIBLE
                binding.gistsRv.visibility = View.GONE
                binding.gistsNoContentInclude.root.visibility = View.GONE
            }
            VisibilityStatus.CONTENT -> {
                binding.gistsPb.visibility = View.GONE
                binding.gistsRv.visibility = View.VISIBLE
                binding.gistsNoContentInclude.root.visibility = View.GONE
            }
            VisibilityStatus.NO_CONTENT -> {
                binding.gistsPb.visibility = View.GONE
                binding.gistsRv.visibility = View.GONE
                binding.gistsNoContentInclude.root.visibility = View.VISIBLE
            }
        }
    }

    private fun goToDetail(gist: Gist) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.GIST_PARCELABLE, gist)
        startActivity(intent)
    }
}