package haroldolivieri.moviescatalog.features.movies

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.features.favorites.FavoritesRepository
import haroldolivieri.moviescatalog.features.favorites.FavoritesRepositoryLocal
import io.realm.RealmConfiguration

@Module
class MoviesModule {
    @Provides
    fun provideView(activity: MainActivity): MainView = activity

    @Provides
    fun providePresenter(presenter: MainPresenterImpl): MainPresenter = presenter

    @Provides
    fun provideMovieRepository(moviesRepository: MoviesRepositoryRemote)
            : MoviesRepository = moviesRepository

    @Provides
    fun provideFavoriteRepository(realmConfiguration: RealmConfiguration)
            : FavoritesRepository = FavoritesRepositoryLocal(realmConfiguration)
}