package haroldolivieri.moviescatalog.local.entities

import haroldolivieri.moviescatalog.domain.Movie
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class MovieRealmObject(@PrimaryKey var id: Int?,
                            var favoredAt: Date?,
                            var releaseDate: Date?,
                            var voteAverage: Double?,
                            var title: String?,
                            var popularity: Double?,
                            var backDropPath: String?,
                            var adult: Boolean?,
                            var genres: RealmList<GenreRealmObject>?,
                            var overview: String?) : RealmObject() {

    constructor() : this(null, null, null, null, null, null, null, null, null, null)

    fun toMovie(): Movie =
            Movie(id, true, voteAverage, title, popularity, backDropPath, adult,
                    genres?.map { it.toGenre() }, releaseDate, overview)
}