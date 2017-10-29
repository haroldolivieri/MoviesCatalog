package haroldolivieri.moviescatalog.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import haroldolivieri.TestMoviesCatalogApplication
import haroldolivieri.moviescatalog.feature.details.TestDetailsModule
import haroldolivieri.moviescatalog.feature.movies.MoviePresenterTest
import haroldolivieri.moviescatalog.feature.movies.TestMoviesModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        TestApplicationModule::class,
        TestMoviesModule::class,
        TestDetailsModule::class))
interface TestApplicationComponent : AndroidInjector<TestMoviesCatalogApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TestApplicationComponent
    }

    fun inject(moviePresenterTest: MoviePresenterTest)
}