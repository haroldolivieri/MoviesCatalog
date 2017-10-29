package haroldolivieri.moviescatalog.features.details

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.features.movies.MainPresenter
import haroldolivieri.moviescatalog.features.movies.MainPresenterImpl
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.local.FavoritesRepositoryLocal
import haroldolivieri.moviescatalog.repository.remote.MoviesRepository
import haroldolivieri.moviescatalog.repository.remote.MoviesRepositoryRemote
import io.realm.RealmConfiguration

@Module
class DetailsModule {
    @Provides
    fun provideView(activity: DetailsActivity): DetailsView = activity

    @Provides
    fun providePresenter(presenter: DetailsPresenterImpl): DetailsPresenter = presenter
}