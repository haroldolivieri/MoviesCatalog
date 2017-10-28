package haroldolivieri.moviescatalog.local

import io.realm.Realm
import io.realm.RealmConfiguration

abstract class AbstractRealmRepository(private val configuration: RealmConfiguration) {
    fun realm(): Realm = Realm.getInstance(configuration)
}