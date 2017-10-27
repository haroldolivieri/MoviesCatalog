package haroldolivieri.moviescatalog.features.main

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.remote.MoviesAPI
import retrofit2.Retrofit

@Module
class MainActivityModule {
    @Provides
    fun provideView(activity: MainActivity): MainView = activity

    @Provides
    fun providePresenter(presenter: MainPresenterImpl): MainPresenter = presenter

    @Provides
    fun provideMovieRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository =
            moviesRepository
}