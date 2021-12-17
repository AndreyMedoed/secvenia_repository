package com.example.networking.fragments.film_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.networking.R
import com.example.networking.databinding.MovieDetailLayoutBinding

class DetailFragment : Fragment(R.layout.movie_detail_layout) {
    private val args: DetailFragmentArgs by navArgs()
    private val binding: MovieDetailLayoutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()

    }

    private fun bind() {
        binding.nameFilm.text = args.film.enName
        binding.descriptionTextView.text = args.film.description

        Glide.with(requireContext())
            .load(args.film.poster.url)
            .placeholder(R.drawable.ic_cloud_download_24)
            .error(R.drawable.ic_error)
            .into(binding.posterImageView)
        binding.ratingTextView.text = getString(R.string.rating, args.film.rating?.kp.toString() ?: "-")
        binding.yearTextView.text = getString(R.string.year, args.film.year.toString() ?: "-")
    }
}