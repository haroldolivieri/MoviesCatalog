package haroldolivieri.moviescatalog.feature.movies

import android.support.v7.widget.RecyclerView
import haroldolivieri.TestMoviesCatalogApplication
import haroldolivieri.moviescatalog.*
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.movies.MoviesPresenter
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import org.mockito.Mockito




@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = TestMoviesCatalogApplication::class)

class MoviePresenterTest {

    private lateinit var recyclerView: RecyclerView
    @Inject lateinit var presenter: MoviesPresenter

    @Before
    fun setUp() {
        TestMoviesCatalogApplication.testApplicationComponent.inject(this)
    }

    @Test
    fun fetchDataWithSuccess() {
        presenter.fetchPopularMoviesData()

        verify(movieViewMock, atLeastOnce()).showGenres(genres)
        verify(movieViewMock, atLeastOnce()).showMovies(moviePage1Matched)
        verify(movieViewMock, atLeastOnce()).getGenresToFilter()
    }

    @Test
    fun onCreationPresenter() {
        val presenterSpy = Mockito.spy<MoviesPresenter>(presenter)
        presenterSpy.onCreate()

        val filterGenres = HashMap<Int, Boolean>()
        genres.map { it.id?.let { it1 -> filterGenres.put(it1, true) } }
        `when`(movieViewMock.getGenresToFilter()).thenReturn(filterGenres)

        verify(presenterSpy, times(1)).fetchPopularMoviesData()
    }

    @Test
    fun favoriteMovie() {
        presenter.fetchPopularMoviesData()
        presenter.favoriteAction(true, moviePage1Matched[0])

        verify(favoriteRepositoryMock, times(1)).favorite(moviePage1Matched[0])
    }

    @Test
    fun unfavoriteMovie() {
        presenter.fetchPopularMoviesData()
        presenter.favoriteAction(false, moviePage1Matched[0])

        verify(favoriteRepositoryMock, times(1)).unfavorite(moviePage1Matched[0].id!!)
    }


    @Test
    fun fetchDataWithFilterSuccess() {
        presenter.fetchPopularMoviesData()
        presenter.performMovieFilter(HashMap())
        val filterByThriller = HashMap<Int, Boolean>()
        filterByThriller.put(1, true)

        val filteredByThrillerResult = listOf(
                Movie(1, true, 0.0, "title1", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(2, "Action")), date, ""),
                Movie(3, false, 0.0, "title3", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(3, "Drama")), date, ""))

        presenter.performMovieFilter(filterByThriller)

        verify(movieViewMock, atLeastOnce()).showMovies(moviePage1Matched)
        verify(movieViewMock, atLeastOnce()).showMovies(emptyList())
        verify(movieViewMock, atLeastOnce()).showMovies(filteredByThrillerResult)
    }
}