package com.example.networking.fragments.list_films

import androidx.paging.*
import com.example.networking.network.NetworkConfig
import com.example.networking.essences.Item
import com.example.networking.paging.FilmPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow


class ListFilmsRepository {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @ExperimentalPagingApi
    fun searchFilms(
        genre: String,
    ): Flow<PagingData<Item>> {
        return Pager(PagingConfig(pageSize = 25)) {
            FilmPagingSource(NetworkConfig.unsplashApi, genre)
        }.flow.cachedIn(scope)
    }

}