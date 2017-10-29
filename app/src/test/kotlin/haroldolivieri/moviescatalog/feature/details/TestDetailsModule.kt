package haroldolivieri.moviescatalog.feature.details

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.features.details.DetailsPresenter
import haroldolivieri.moviescatalog.features.details.DetailsPresenterImpl
import haroldolivieri.moviescatalog.features.details.DetailsView
import org.mockito.Mockito.mock

@Module
class TestDetailsModule {
    @Provides
    fun provideView(): DetailsView = mock(DetailsView::class.java)

    @Provides
    fun providePresenter(presenter: DetailsPresenterImpl): DetailsPresenter = presenter
}