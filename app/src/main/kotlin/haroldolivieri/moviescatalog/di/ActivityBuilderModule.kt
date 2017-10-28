package haroldolivieri.moviescatalog.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import haroldolivieri.moviescatalog.features.movies.MainActivity
import haroldolivieri.moviescatalog.features.movies.MoviesModule

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = arrayOf(MoviesModule::class, RemoteModule::class))
    internal abstract fun bindMainActivity(): MainActivity
}