package haroldolivieri.moviescatalog

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import haroldolivieri.moviescatalog.di.ApplicationComponent
import haroldolivieri.moviescatalog.di.DaggerApplicationComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class MoviesCatalogApplication : DaggerApplication() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("udrink.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()

        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .realmConfiguration(realmConfig)
                .build()

        applicationComponent.inject(this)
        return applicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        calligraphyConfig()
    }

    private fun calligraphyConfig() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.bariol_regular))
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}