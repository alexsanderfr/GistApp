package com.alexsanderfranco.gistapp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.detail.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {
    private val _gist = MutableLiveData<Gist>()

    val gist: LiveData<Gist> get() = _gist

    fun setGist(newGist: Gist) {
        _gist.value = newGist
    }

    fun onClickFavorite(gist: Gist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(gist)
            gist.isFavorite = !gist.isFavorite
            _gist.postValue(gist)
        }
    }

}