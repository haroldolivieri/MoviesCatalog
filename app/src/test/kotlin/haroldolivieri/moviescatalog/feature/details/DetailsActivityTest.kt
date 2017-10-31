package haroldolivieri.moviescatalog.feature.details

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import android.widget.TextView
import haroldolivieri.TestMoviesCatalogApplication
import haroldolivieri.moviescatalog.BuildConfig
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.TestFaker.Companion.fakerSubject
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage1MatchedFaked
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.details.DetailsActivity
import haroldolivieri.moviescatalog.features.details.DetailsIntent
import haroldolivieri.moviescatalog.features.movies.MovieAdapter
import haroldolivieri.moviescatalog.features.movies.MoviesActivity
import haroldolivieri.moviescatalog.features.movies.formatToString
import haroldolivieri.moviescatalog.repository.local.FavoredEvent
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication
import com.bumptech.glide.Glide.tearDown
import org.junit.After


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = TestMoviesCatalogApplication::class)
class DetailsActivityTest {

    private lateinit var moviesActivity: MoviesActivity
    private lateinit var detailsActivity: DetailsActivity

    @Before
    fun setup() {
        moviesActivity = Robolectric.setupActivity(MoviesActivity::class.java)
    }

    @Test
    fun validDataOnIntentExtra() {
        val movie = moviePage1MatchedFaked[0]
        val (movieYear, movieOverView, movieVoteAverage) = getViewElements(movie)

        assertTrue(movieYear.text.toString() == movie.releaseDate?.formatToString("yyyy"))
        assertTrue(movieOverView.text.toString() == movie.overview)
        assertTrue(movieVoteAverage.text.toString() == movie.voteAverage.toString())
    }

    @Test
    fun nullDataOnIntentExtra() {
        val movie = Movie(null, null, null, null, null, null, null, null, null, null)
        val (movieYear, movieOverView, movieVoteAverage) = getViewElements(movie)

        assertTrue(movieYear.text.toString() == "Not Informed")
        assertTrue(movieOverView.text.toString() == "Not Informed")
        assertTrue(movieVoteAverage.text.toString() == "-")
    }

    @Test
    fun favoriteInDetailsActivityShouldChangeMovieActivityItemList() {
        val movie = moviePage1MatchedFaked[0]
        checkFavoriteBehavior(movie)
    }

    @Test
    fun backButtonBehavior() {
        startDetailsActivityWithExtras(moviePage1MatchedFaked[0])
        val activityShadow = Shadows.shadowOf(detailsActivity)
        detailsActivity.onBackPressed()
        assertTrue(activityShadow.isFinishing)
    }

    private fun getViewElements(movie: Movie): Triple<TextView, TextView, TextView> {
        startDetailsActivityWithExtras(movie)

        val movieYear = (detailsActivity.findViewById<TextView>(R.id.movieYear))
        val movieOverView = (detailsActivity.findViewById<TextView>(R.id.movieOverView))
        val movieVoteAverage = (detailsActivity.findViewById<TextView>(R.id.movieVoteAverage))
        return Triple(movieYear, movieOverView, movieVoteAverage)
    }

    private fun checkFavoriteBehavior(movie: Movie) {
        startDetailsActivityWithExtras(movie)

        val favCheckBox = detailsActivity.findViewById<CheckBox>(R.id.favoriteButton)
        assertTrue(favCheckBox.isChecked == movie.favored)

        favCheckBox.performClick()
        fakerSubject.onNext(FavoredEvent(!movie.favored!!, movie.id!!))
        assertTrue(favCheckBox.isChecked == !movie.favored!!)

        detailsActivity.onBackPressed()

        val moviesRecyclerView = moviesActivity.findViewById<RecyclerView>(R.id.moviesRecyclerView)
        moviesRecyclerView.scrollToPosition(0)
        val moviesViewHolder = moviesRecyclerView.findViewHolderForAdapterPosition(0)
                as MovieAdapter.MoviesViewHolder

        assertTrue(moviesViewHolder.favAction.isChecked == favCheckBox.isChecked)
    }


    private fun startDetailsActivityWithExtras(movie: Movie) {
        moviesActivity.startActivity(moviesActivity.DetailsIntent(movie))

        val intent = Intent(ShadowApplication.getInstance()
                .applicationContext, DetailsActivity::class.java)
                .putExtra(DetailsActivity.INTENT_MOVIE_SELECTED, movie)

        detailsActivity = Robolectric
                .buildActivity(DetailsActivity::class.java)
                .withIntent(intent)
                .create()
                .get()

        assertNotNull(detailsActivity)
    }

    @After
    fun tearDown() {
        Robolectric.reset()
    }
}