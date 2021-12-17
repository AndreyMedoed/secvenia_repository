package com.example.networking.fragments.list_films

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.networking.essences.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class ListFilmsViewModel : ViewModel() {
    private val repository = ListFilmsRepository()

    private val progBarEvent = MutableLiveData<Boolean>(false)

    @ExperimentalPagingApi
    fun searchFilms(genre: String): Flow<PagingData<Item>> {

        val flow = repository.searchFilms(genre)

        return flow.onEach {
            showProgress(false)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            progBarEvent.postValue(true)
        } else {
            progBarEvent.postValue(false)
        }
    }
}