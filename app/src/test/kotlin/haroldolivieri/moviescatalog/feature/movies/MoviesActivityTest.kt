package haroldolivieri.moviescatalog.feature.movies

import android.support.v7.widget.RecyclerView
import haroldolivieri.TestMoviesCatalogApplication
import haroldolivieri.moviescatalog.BuildConfig
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.TestFaker.Companion.genresFaked
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage1MatchedFaked
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage2MatchedFaked
import haroldolivieri.moviescatalog.features.details.DetailsActivity
import haroldolivieri.moviescatalog.features.movies.GenreAdapter
import haroldolivieri.moviescatalog.features.movies.MovieAdapter
import haroldolivieri.moviescatalog.features.movies.MoviesActivity
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = TestMoviesCatalogApplication::class)
class MoviesActivityTest {

    private lateinit var moviesActivity: MoviesActivity
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var genresRecyclerView: RecyclerView

    @Before
    fun setUp() {
        moviesActivity = Robolectric.setupActivity(MoviesActivity::class.java)
        moviesRecyclerView = moviesActivity.findViewById<RecyclerView>(R.id.moviesRecyclerView)
        genresRecyclerView = moviesActivity.findViewById<RecyclerView>(R.id.genreRecyclerView)
    }

    @Test
    fun recyclersShouldNotBeNull() {
        assertNotNull(moviesRecyclerView)
        assertNotNull(genresRecyclerView)
    }

    @Test
    fun recyclerViewItemCount() {
        val page1Count = moviePage1MatchedFaked.size
        val page2Count = moviePage2MatchedFaked.size

        assertTrue(page1Count + page2Count == moviesActivity.movieAdapter.itemCount)
        assertTrue(genresFaked.size == moviesActivity.genreAdapter.itemCount)
    }

    @Test
    fun assertAdapterMovieItemBehavior() {
        moviesRecyclerView.scrollToPosition(0)
        val moviesViewHolder = moviesRecyclerView.findViewHolderForAdapterPosition(0)
                as MovieAdapter.MoviesViewHolder

        val movie = moviePage1MatchedFaked[0]

        assertTrue(moviesViewHolder.title.text == movie.title)
        assertTrue(moviesViewHolder.favAction.isChecked == movie.favored)
        moviesViewHolder.favAction.performClick()
        assertTrue(moviesViewHolder.favAction.isChecked == !movie.favored!!)
    }

    @Test
    fun assertAdapterGenreItemBehavior() {
        genresRecyclerView.scrollToPosition(0)
        val genresViewHolder = genresRecyclerView.findViewHolderForAdapterPosition(0)
                as GenreAdapter.GenreViewHolder

        val isChecked = moviesActivity.getGenresToFilter().any { it.key == genresFaked[0].id }

        assertTrue(genresViewHolder.categoryCheckBox.text == genresFaked[0].name)
        assertTrue(genresViewHolder.categoryCheckBox.isChecked == isChecked)
        genresViewHolder.categoryCheckBox.performClick()
        assertTrue(genresViewHolder.categoryCheckBox.isChecked == !isChecked)
    }

    @Test
    fun onMovieItemClickedTest() {
        moviesRecyclerView.scrollToPosition(0)
        moviesRecyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        val shadowActivity = shadowOf(moviesActivity)
        val startedIntent = shadowActivity.nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertThat(shadowIntent.intentClass.name, equalTo(DetailsActivity::class.java.name))
    }

}