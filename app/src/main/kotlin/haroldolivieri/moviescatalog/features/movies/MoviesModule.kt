package haroldolivieri.moviescatalog.features.movies

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.local.FavoritesRepositoryLocal
import haroldolivieri.moviescatalog.repository.remote.MoviesRepository
import haroldolivieri.moviescatalog.repository.remote.MoviesRepositoryRemote
import io.realm.RealmConfiguration

@Module
class MoviesModule {
    @Provides
    fun provideView(activity: MoviesActivity): MainView = activity

    @Provides
    fun providePresenter(presenter: MainPresenterImpl): MainPresenter = presenter

    @Provides
    fun provideMovieRepository(moviesRepository: MoviesRepositoryRemote)
            : MoviesRepository = moviesRepository

    @Provides
    fun provideFavoriteRepository(realmConfiguration: RealmConfiguration)
            : FavoritesRepository = FavoritesRepositoryLocal(realmConfiguration)
}