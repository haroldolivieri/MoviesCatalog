package haroldolivieri.moviescatalog.feature.movies

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.TestFaker.Companion.genresFaked
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage1Faked
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage2Faked
import haroldolivieri.moviescatalog.di.SchedulerProvider
import haroldolivieri.moviescatalog.di.qualifier.TestScheduler
import haroldolivieri.moviescatalog.features.movies.MoviesActivity
import haroldolivieri.moviescatalog.features.movies.MoviesPresenter
import haroldolivieri.moviescatalog.features.movies.MoviesPresenterImpl
import haroldolivieri.moviescatalog.features.movies.MoviesView
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
    fun provideView(activity: MoviesActivity): MoviesView = activity

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

        `when`(moviesAPI.getGenres(apiKey)).thenReturn(Observable.just(genresFaked))
        `when`(moviesAPI.getPopularMovies(apiKey, 1)).thenReturn(Observable.just(moviePage1Faked))
        `when`(moviesAPI.getPopularMovies(apiKey, 2)).thenReturn(Observable.just(moviePage2Faked))

        return MoviesRepositoryRemote(moviesAPI, apiKey)
    }

}