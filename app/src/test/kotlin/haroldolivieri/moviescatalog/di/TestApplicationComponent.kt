package haroldolivieri.moviescatalog.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import haroldolivieri.TestMoviesCatalogApplication
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        TestApplicationModule::class,
        TestActivityBuilderModule::class))
interface TestApplicationComponent : AndroidInjector<TestMoviesCatalogApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TestApplicationComponent
    }
}