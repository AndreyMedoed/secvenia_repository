package com.example.networking.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networking.R
import com.example.networking.databinding.GenreLayoutBinding
import com.example.networking.databinding.MovieLayoutBinding
import com.example.networking.databinding.TitleLayoutBinding
import com.example.networking.essences.*


class PagingItemAdapter(
    private val context: Context,
    private val openFilmDetails: ((photo: Film) -> Unit),
    private val onItemClick: ((genre: Genre, binding: GenreLayoutBinding) -> Unit)
) : PagingDataAdapter<Item, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            when {
                holder is FilmHolder && item is Film -> holder.bind(item)
                holder is GenreHolder && item is Genre -> holder.bind(item)
                holder is TitleHolder && item is Title -> holder.bind(item)

            }
        } else {
            /** Если приходит null, то обозначаем холдер как FilmHolder и
             * даем ему заглушку в виде пустого экзенпляра класса Film */
            (holder as FilmHolder).bind(
                Film(
                    0,
                    "",
                    "",
                    "",
                    Poster("", ""),
                    0,
                    Rating(0.0),
                    ""
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Film -> FILM_CONSTANT
            is Genre -> GENRE_CONSTANT
            is Title -> TITLE_CONSTANT
            null -> NULL_CONSTANT
            else -> {
                error("Некорректный элемент списка, нельзя сгенерировать ViewType")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val photoBinding =
            MovieLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val genreBinding =
            GenreLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val titleBinding =
            TitleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            FILM_CONSTANT -> FilmHolder(
                photoBinding,
                openFilmDetails
            )
            GENRE_CONSTANT -> GenreHolder(
                genreBinding,
                onItemClick,
                context
            )
            TITLE_CONSTANT -> TitleHolder(
                titleBinding
            )
            NULL_CONSTANT -> FilmHolder(photoBinding) { }
            else -> error("Неверный ViewType в функции onCreateViewHolder")
        }
    }

    class ItemDiffUtilCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return when {
                oldItem is Film && newItem is Film -> oldItem.id == newItem.id
                oldItem is Genre && newItem is Genre -> oldItem.name == newItem.name
                oldItem is Title && newItem is Title -> oldItem.name == newItem.name
                else -> false
            }
        }


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return when {
                oldItem is Film && newItem is Film -> oldItem == newItem
                oldItem is Genre && newItem is Genre -> oldItem == newItem
                oldItem is Title && newItem is Title -> oldItem == newItem
                else -> false
            }
        }
    }

    class FilmHolder(
        private var binding: MovieLayoutBinding,
        private val openFilmDetails: ((photo: Film) -> Unit)
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.filmImage.setOnClickListener {
                openFilmDetails(film)
            }

            binding.nameFilm.text = if (film.name == null) {
                film.enName ?: "null"
            } else {
                film.name
            }

            Glide.with(itemView)
                .load(film.poster.url)
                .into(binding.filmImage)
        }
    }

    class GenreHolder(
        private val binding: GenreLayoutBinding,
        private val onItemClick: ((genre: Genre, binding: GenreLayoutBinding) -> Unit),
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.genreMaterialCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (genre.isSelected) {
                        R.color.genre_selected_item_background
                    } else {
                        R.color.genre_item_background
                    }
                )
            )
            binding.genreMaterialCard.setOnClickListener {
                onItemClick(genre, binding)
            }
            binding.genreName.text = genre.name
        }
    }

    class TitleHolder(
        private val binding: TitleLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: Title) {
            binding.titleName.text = title.name
        }
    }

    companion object {
        private const val FILM_CONSTANT = 1
        private const val GENRE_CONSTANT = 2
        private const val NULL_CONSTANT = 3
        private const val TITLE_CONSTANT = 4
    }
}