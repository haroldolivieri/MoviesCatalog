package haroldolivieri.moviescatalog.feature.movies

import haroldolivieri.moviescatalog.TestFaker.Companion.fakerDate
import haroldolivieri.moviescatalog.TestFaker.Companion.genresFaked
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage1MatchedFaked
import haroldolivieri.moviescatalog.TestSchedulerProvider
import haroldolivieri.moviescatalog.di.TestApplicationModule
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.movies.MoviesPresenter
import haroldolivieri.moviescatalog.features.movies.MoviesPresenterImpl
import haroldolivieri.moviescatalog.features.movies.MoviesView
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class MoviesPresenterTest {

    val filterGenres = HashMap<Int, Boolean>()
    val movieViewMock: MoviesView = mock(MoviesView::class.java)
    val moviesRepositoryMock = TestMoviesModule().provideMovieRepository()
    val favoriteRepositoryMock = TestApplicationModule().provideFavoriteRepository()

    lateinit var presenter: MoviesPresenter

    @Before
    fun setUp() {
        genresFaked.map { it.id?.let { it1 -> filterGenres.put(it1, true) } }
        `when`(movieViewMock.getGenresToFilter()).thenReturn(filterGenres)

        presenter = MoviesPresenterImpl(movieViewMock,
                moviesRepositoryMock,
                Observable.empty(),
                favoriteRepositoryMock,
                TestSchedulerProvider())
    }

    @Test
    fun fetchDataWithSuccess() {
        presenter.fetchPopularMoviesData()

        verify(movieViewMock, times(1)).showGenres(genresFaked)
        verify(movieViewMock, times(1)).showMovies(moviePage1MatchedFaked)
        verify(movieViewMock, times(1)).getGenresToFilter()
    }

    @Test
    fun onCreationPresenter() {
        val presenterSpy = Mockito.spy<MoviesPresenter>(presenter)
        presenterSpy.onConnected()

        val filterGenres = HashMap<Int, Boolean>()
        genresFaked.map { it.id?.let { it1 -> filterGenres.put(it1, true) } }
        `when`(movieViewMock.getGenresToFilter()).thenReturn(filterGenres)

        verify(presenterSpy, times(1)).fetchPopularMoviesData()
    }

    @Test
    fun favoriteMovie() {
        presenter.fetchPopularMoviesData()
        presenter.favoriteAction(true, moviePage1MatchedFaked[0])

        verify(favoriteRepositoryMock, times(1)).favorite(moviePage1MatchedFaked[0])
    }

    @Test
    fun unfavoriteMovie() {
        presenter.fetchPopularMoviesData()
        presenter.favoriteAction(false, moviePage1MatchedFaked[0])

        verify(favoriteRepositoryMock, times(1)).unfavorite(moviePage1MatchedFaked[0].id!!)
    }

    @Test
    fun fetchDataWithFilterSuccess() {
        presenter.fetchPopularMoviesData()
        presenter.performMovieFilter(HashMap())
        val filterByThriller = HashMap<Int, Boolean>()
        filterByThriller.put(1, true)

        val filteredByThrillerResult = listOf(
                Movie(1, true, 0.0, "title1", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(2, "Action")), fakerDate, ""),
                Movie(3, false, 0.0, "title3", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(3, "Drama")), fakerDate, ""))

        presenter.performMovieFilter(filterByThriller)

        verify(movieViewMock, times(1)).showMovies(moviePage1MatchedFaked)
        verify(movieViewMock, times(1)).showMovies(emptyList())
        verify(movieViewMock, times(1)).showMovies(filteredByThrillerResult)
    }
}