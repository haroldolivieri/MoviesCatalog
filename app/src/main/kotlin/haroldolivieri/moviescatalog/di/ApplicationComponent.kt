package haroldolivieri.moviescatalog.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import haroldolivieri.moviescatalog.MoviesCatalogApplication
import io.realm.RealmConfiguration
import io.realm.internal.OsRealmConfig
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityBuilderModule::class))
abstract class ApplicationComponent : AndroidInjector<MoviesCatalogApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun realmConfiguration(realmConfig: RealmConfiguration): Builder
        fun build(): ApplicationComponent
    }
}