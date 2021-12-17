package com.example.networking.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networking.utils.EmptyListException
import com.example.networking.network.UnsplashApi
import com.example.networking.essences.Genre
import com.example.networking.essences.Item
import com.example.networking.essences.Title
import retrofit2.HttpException

/** Этот источник используется тольуо для "Поиска" фотографий по ключевым словам. */
class FilmPagingSource(
    private val unsplashApi: UnsplashApi,
    private val genre: String
) : PagingSource<Int, Item>() {

    private var startList = listOf<Item>(
        Title("Жанры"),
        Genre("драма", false),
        Genre("фэнтези", false),
        Genre("криминал", false),
        Genre("детектив", false),
        Genre("мелодрама", false),
        Genre("биография", false),
        Genre("комедия", false),
        Genre("фантастика", false),
        Genre("боевик", false),
        Genre("триллер", false),
        Genre("мюзикл", false),
        Genre("приключения", false),
        Genre("ужасы", false),
        Title("Фильмы")
    )

    private val listWithSelected = startList.map { item ->
        if (item is Genre && item.name == genre) {
            item.isSelected = true
        }
        item
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response =
                unsplashApi.getFilmList(
                    genre = genre,
                    page = pageNumber.toString(),
                    pageSize = pageSize.toString()
                )

            val filmList: List<Item>? = if (pageNumber == INITIAL_PAGE_NUMBER) {
                listWithSelected + response.body()?.filmList as List<Item>
            } else {
                response.body()?.filmList
            }

            /** Если на 1 странице пусто, значит выдаем кастомную ошибку - чтоб обработать пустой список*/
            if (pageNumber == INITIAL_PAGE_NUMBER && filmList.isNullOrEmpty()) throw EmptyListException()
            val nextPageNumber = if (filmList?.isEmpty()!!) null else pageNumber + 1
            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
            return LoadResult.Page(filmList, prevPageNumber, nextPageNumber)
        } catch (e: EmptyListException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }


    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}