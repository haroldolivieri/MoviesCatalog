package haroldolivieri.moviescatalog.local.entities

import haroldolivieri.moviescatalog.remote.entities.Genre
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class GenreRealmObject(var id: Int?,
                            var name: String?) : RealmObject() {

    constructor() : this(null, null)
    fun toGenre(): Genre = Genre(id, name)
}