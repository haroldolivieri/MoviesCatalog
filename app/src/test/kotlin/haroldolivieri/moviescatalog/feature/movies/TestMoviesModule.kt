package haroldolivieri.moviescatalog.feature.movies

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.TestSchedulerProvider
import haroldolivieri.moviescatalog.di.SchedulerProvider
import haroldolivieri.moviescatalog.di.qualifier.TestScheduler
import haroldolivieri.moviescatalog.features.movies.MoviesPresenter
import haroldolivieri.moviescatalog.features.movies.MoviesPresenterImpl
import haroldolivieri.moviescatalog.features.movies.MoviesView
import haroldolivieri.moviescatalog.genres
import haroldolivieri.moviescatalog.movieViewMock
import haroldolivieri.moviescatalog.moviePage1
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.remote.MoviesAPI
import haroldolivieri.moviescatalog.repository.remote.MoviesRepository
import haroldolivieri.moviescatalog.repository.remote.MoviesRepositoryRemote
import io.reactivex.Observable
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@Module
class TestMoviesModule {
    @Provides
    fun provideView(): MoviesView {
        val filterGenres = HashMap<Int, Boolean>()
        genres.map { it.id?.let { it1 -> filterGenres.put(it1, true) } }

        `when`(movieViewMock.getGenresToFilter()).thenReturn(filterGenres)
        return movieViewMock
    }

    @Provides
    fun providePresenter(mainView: MoviesView,
                         moviesRepository: MoviesRepository,
                         favoritesRepository: FavoritesRepository,
                         @TestScheduler schedulerProvider: SchedulerProvider): MoviesPresenter =
            MoviesPresenterImpl(mainView, moviesRepository, favoritesRepository, schedulerProvider)

    @Provides
    fun provideMovieRepository(): MoviesRepository {
        val moviesAPI = mock(MoviesAPI::class.java)
        val apiKey = "12345678"

        `when`(moviesAPI.getGenres(apiKey)).thenReturn(Observable.just(genres))
        `when`(moviesAPI.getPopularMovies(apiKey, 1)).thenReturn(Observable.just(moviePage1))

        return MoviesRepositoryRemote(moviesAPI, apiKey)
    }

}