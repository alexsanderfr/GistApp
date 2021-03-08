package com.alexsanderfranco.gistapp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.list.repository.GistListRepository
import com.alexsanderfranco.gistapp.shared.test.SimpleIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GistListViewModel(
    private val repository: GistListRepository,
    private val idlingResource: SimpleIdlingResource
) : ViewModel() {
    private val _gistList = MutableLiveData<List<Gist>>()
    private val _isLoading = MutableLiveData(true)
    private val _showFavorites = MutableLiveData(false)

    private var isLoadingMore = false
    private var query: String? = null
    private var page = 1

    val gistList: LiveData<List<Gist>> get() = _gistList
    val isLoading: LiveData<Boolean> get() = _isLoading
    val showFavorites: LiveData<Boolean> get() = _showFavorites

    fun fetchGistList() {
        setLoading(true)
        page = 1
        viewModelScope.launch(Dispatchers.IO) {
            val gists = repository.fetchGistList(page)
            setLoading(false)
            _gistList.postValue(filterForQuery(gists))
        }
    }

    fun fetchFavorites() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val gists = repository.fetchFavorites()
            setLoading(false)
            _gistList.postValue(filterForQuery(gists))
        }
    }

    fun loadMore() {
        if (!isLoadingMore) {
            isLoadingMore = true
            page++
            viewModelScope.launch(Dispatchers.IO) {
                val gists = repository.fetchGistList(page)
                withContext(Dispatchers.Main) {
                    isLoadingMore = false
                    val currentGists = mutableListOf<Gist>()
                    _gistList.value?.let { currentGists.addAll(it) }
                    currentGists.addAll(gists)
                    _gistList.value = filterForQuery(currentGists)
                }
            }
        }
    }

    fun onClickFavorite(gist: Gist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(gist)
            updateGistList(gist)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
        idlingResource.setIdleState(!isLoading)
    }

    fun setShowFavorites(newValue: Boolean) {
        _showFavorites.value = newValue
    }

    fun getQuery(): String? = query

    fun setQuery(newValue: String?) {
        this.query = newValue
        if (showFavorites.value == true) {
            fetchFavorites()
        } else {
            fetchGistList()
        }
    }

    private fun filterForQuery(gists: List<Gist>): List<Gist> {
        return if (query == null) gists
        else gists.filter { gist ->
            query?.let {
                gist.owner?.login?.contains(
                    it, ignoreCase = true
                )
            } == true
        }
    }

    private fun updateGistList(gist: Gist) {
        _gistList.value?.let { gists ->
            gists.forEach { item ->
                if (item == gist) {
                    item.isFavorite = !item.isFavorite
                }
            }
            _gistList.postValue(gists)
        }
    }
}