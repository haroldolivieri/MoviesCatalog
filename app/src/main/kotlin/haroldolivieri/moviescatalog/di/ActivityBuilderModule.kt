package haroldolivieri.moviescatalog.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import haroldolivieri.moviescatalog.features.main.MainActivity
import haroldolivieri.moviescatalog.features.main.MainActivityModule

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class, RemoteModule::class))
    internal abstract fun bindMainActivity(): MainActivity
}