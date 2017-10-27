package haroldolivieri.moviescatalog.features.main

import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.MainActivity

@Module
abstract class MainActivityModule {
    @Provides
    fun provideView(activity: MainActivity): MainView = activity

    @Provides
    fun providePresenter(presenter: MainPresenterImpl): MainPresenter = presenter
}