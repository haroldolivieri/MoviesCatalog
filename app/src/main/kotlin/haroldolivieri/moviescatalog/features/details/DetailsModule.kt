package haroldolivieri.moviescatalog.features.details

import dagger.Module
import dagger.Provides

@Module
class DetailsModule {
    @Provides
    fun provideView(activity: DetailsActivity): DetailsView = activity

    @Provides
    fun providePresenter(presenter: DetailsPresenterImpl): DetailsPresenter = presenter
}