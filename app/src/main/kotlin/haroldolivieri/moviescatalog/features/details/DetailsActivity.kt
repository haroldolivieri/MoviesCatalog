package haroldolivieri.moviescatalog.features.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.BaseActivity
import haroldolivieri.moviescatalog.features.details.DetailsActivity.Companion.INTENT_MOVIE_SELECTED
import haroldolivieri.moviescatalog.features.movies.formatToString
import kotlinx.android.synthetic.main.activity_movie_details.*
import javax.inject.Inject

fun Context.DetailsIntent(movie: Movie): Intent {
    return Intent(this, DetailsActivity::class.java).apply {
        putExtra(INTENT_MOVIE_SELECTED, movie)
    }
}

interface DetailsView

class DetailsActivity(override val layout: Int = R.layout.activity_movie_details) :
        BaseActivity(), DetailsView {

    @Inject lateinit var detailsPresenter: DetailsPresenter

    companion object {
        val INTENT_MOVIE_SELECTED = "movie"
    }

    val movie by lazy {
        intent.getSerializableExtra(INTENT_MOVIE_SELECTED) as Movie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        populateInfo()
        setupFavAction()
        shareAction.setOnClickListener { setupShareAction() }
    }

    private fun setupShareAction() {
        val s = Intent(android.content.Intent.ACTION_SEND)
        s.type = "text/plain"
        s.putExtra(Intent.EXTRA_SUBJECT, "Movie Recommendation")
        s.putExtra(Intent.EXTRA_TEXT, "Movies Catalog - Recommendation\n\n" +
                "Title: ${movie.title}\n" +
                "Release Date: ${movie.releaseDate?.formatToString("yyyy-MM-dd")}\n" +
                "Genres: ${movieGenres.text}\n" +
                "${movie.backDropPath}")

        startActivity(Intent.createChooser(s, "Quote"))
    }

    private fun setupFavAction() {
        favoriteButton.setOnClickListener { v ->
            val checked = (v as CheckBox).isChecked
            detailsPresenter.favoriteAction(checked, movie)
        }
    }

    private fun populateInfo() {
        movie.apply {
            val options = RequestOptions()
                    .priority(Priority.HIGH)

            Glide.with(this@DetailsActivity)
                    .load(movie.backDropPath)
                    .apply(options)
                    .into(movieImage)

            movieTitle.text = title
            movieYear.text = releaseDate?.formatToString("yyyy")
            movieVoteAverage.text = "$voteAverage"
            favoriteButton.isChecked = favored!!
            movieOverView.text = overview

            if (adult!!) {
                movieAdultImage.setImageDrawable(getDrawable(R.drawable.ic_clapperboard_adult))
            } else {
                movieAdultImage.setImageDrawable(getDrawable(R.drawable.ic_clapperboard))
            }

            var strGenres = ""
            genres?.forEach { strGenres += "-${it.name} " }
            movieGenres.text = "Genres: $strGenres"
        }
    }
}
