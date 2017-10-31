package haroldolivieri.moviescatalog.feature.details

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.features.details.DetailsPresenter
import haroldolivieri.moviescatalog.features.details.DetailsPresenterImpl
import haroldolivieri.moviescatalog.features.details.DetailsView
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import org.mockito.Mockito.mock

@Module
class TestDetailsModule {
    @Provides
    fun provideView(): DetailsView = mock(DetailsView::class.java)

    @Provides
    fun providePresenter(detailsView : DetailsView,
                         favoritesRepository: FavoritesRepository): DetailsPresenter =
            DetailsPresenterImpl(detailsView, favoritesRepository)
}