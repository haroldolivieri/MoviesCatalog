package haroldolivieri.moviescatalog.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import haroldolivieri.moviescatalog.feature.details.TestDetailsModule
import haroldolivieri.moviescatalog.feature.movies.TestMoviesModule
import haroldolivieri.moviescatalog.features.details.DetailsActivity
import haroldolivieri.moviescatalog.features.movies.MoviesActivity


@Module
abstract class TestActivityBuilderModule {
    @ContributesAndroidInjector(modules = arrayOf(TestMoviesModule::class))
    internal abstract fun bindMainActivity(): MoviesActivity

    @ContributesAndroidInjector(modules = arrayOf(TestDetailsModule::class))
    internal abstract fun bindDetailsActivity(): DetailsActivity
}