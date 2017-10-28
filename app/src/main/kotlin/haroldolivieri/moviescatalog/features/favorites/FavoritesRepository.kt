package haroldolivieri.moviescatalog.features.favorites

import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.local.AbstractRealmRepository
import haroldolivieri.moviescatalog.local.entities.GenreRealmObject
import haroldolivieri.moviescatalog.local.entities.MovieRealmObject
import haroldolivieri.moviescatalog.remote.entities.Genre
import io.reactivex.Observable
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.Sort
import java.util.*


interface FavoritesRepository {
    fun favorite(movie: Movie)
    fun unFavorite(id: Int)
    fun fetch(): Observable<Movie>
}

class FavoritesRepositoryLocal(configuration: RealmConfiguration) :
        AbstractRealmRepository(configuration), FavoritesRepository {

    companion object {
        private val CLAZZ = MovieRealmObject::class.java
    }

    override fun fetch(): Observable<Movie> {
        realm().use {
            val favorites = it.copyFromRealm(it.where(CLAZZ).findAllSorted("favoredAt", Sort.ASCENDING))
            return Observable.fromIterable(favorites.map(MovieRealmObject::toMovie))
        }
    }

    override fun favorite(movie: Movie) {
        var favorite : MovieRealmObject? = null

        movie.apply {
            val list = RealmList<GenreRealmObject>()
            genres?.forEach { list.add(GenreRealmObject(it.id, it.name)) }

            favorite = MovieRealmObject(id, Date(), voteAverage,
                    title, popularity, backDropPath, adult, list, overview)
        }



        realm().use {
            it.executeTransaction {
                it.copyToRealm(favorite)
            }
        }
    }

    override fun unFavorite(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}