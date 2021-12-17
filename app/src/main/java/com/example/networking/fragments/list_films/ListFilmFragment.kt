package com.example.networking.fragments.list_films

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.networking.R
import com.example.networking.databinding.GenreLayoutBinding
import com.example.networking.databinding.ListFilmFragmentLayoutBinding
import com.example.networking.essences.Film
import com.example.networking.essences.Genre
import com.example.networking.essences.Item
import com.example.networking.essences.Title
import com.example.networking.utils.EmptyListException
import com.example.networking.utils.ItemOffsetDecoration
import com.example.networking.utils.autoCleared
import com.example.networking.utils.isInternetAvailable
import com.example.networking.adapters.ListLoaderStateAdapter
import com.example.networking.adapters.PagingItemAdapter
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListFilmFragment : Fragment(R.layout.list_film_fragment_layout) {

    private val binding: ListFilmFragmentLayoutBinding by viewBinding()
    private val viewModel: ListFilmsViewModel by viewModels()
    private var filmAdapter: PagingItemAdapter by autoCleared()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(
            SHARED_PREFERENCE,
            Context.MODE_PRIVATE
        )
    }

    private val startList = listOf<Item>(
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

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getFilmList(
            sharedPreferences.getString(GENRE_KEY, (startList[1] as Genre).name)
                ?: (startList[1] as Genre).name
        )
    }


    @ExperimentalPagingApi
    private fun getFilmList(genre: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val flow = viewModel.searchFilms(genre)
            flow.collectLatest { filmList ->
                filmAdapter.submitData(filmList)
            }
        }
    }

    @ExperimentalPagingApi
    private fun initAdapter() {
        filmAdapter = PagingItemAdapter(requireContext(),
            { film -> openFilmDetails(film) },
            { genre, binding -> onGenreClick(genre, binding) }
        )
        binding.recyclerViewId.adapter = filmAdapter
        binding.recyclerViewId.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position < startList.size) 2 else 1
                    }
                }
            }
        binding.recyclerViewId.adapter = filmAdapter.withLoadStateHeaderAndFooter(
            ListLoaderStateAdapter(),
            ListLoaderStateAdapter()
        )
        binding.recyclerViewId.addItemDecoration(
            ItemOffsetDecoration(requireContext())
        )
        binding.recyclerViewId.itemAnimator = FlipInTopXAnimator()

        filmAdapter.addLoadStateListener { state: CombinedLoadStates ->
            binding.progressBarId.isVisible = state.source.refresh is LoadState.Loading
            binding.alertTextViewId.isVisible = state.source.refresh is LoadState.Error
            if (binding.alertTextViewId.isVisible) {
                viewLifecycleOwner.lifecycleScope.launch {
                    filmAdapter.submitData(PagingData.from(startList))
                }
                when ((state.source.refresh as LoadState.Error).error) {
                    is EmptyListException -> showAlertText(getString(R.string.alert_text_view_empty_list))
                    else -> showAlertText(getString(R.string.alert_text_view_error))
                }
            }
        }
        if (!isInternetAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.internet_is_missing),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showAlertText(text: String) {
        binding.alertTextViewId.text = text
    }

    private fun openFilmDetails(film: Film) {
        val action = ListFilmFragmentDirections.actionListFilmFragmentToDetailFragment(
            film.name ?: getString(
                R.string.film
            ), film
        )
        findNavController().navigate(action)
    }

    @ExperimentalPagingApi
    private fun onGenreClick(genre: Genre, binding: GenreLayoutBinding) {

        putGenreInSharedPref(genre.name)

        viewLifecycleOwner.lifecycleScope.launch {
            getFilmList(genre.name)
        }
    }

    private fun putGenreInSharedPref(genre: String) {
        sharedPreferences.edit()
            .putString(
                GENRE_KEY,
                genre
            )
            .apply()
    }

    companion object {
        private const val GENRE_KEY = "GENRE_KEY"
        const val SHARED_PREFERENCE = "SHARED_PREFERENCE"

    }
}