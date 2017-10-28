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
    fun favorite(movie: Movie): Observable<Movie>
    fun unFavorite(id: Int)
    fun fetch(): Observable<Movie>
    fun deleteAll()
}

class FavoritesRepositoryLocal(configuration: RealmConfiguration) :
        AbstractRealmRepository(configuration), FavoritesRepository {

    companion object {
        private val CLAZZ_MOVIE = MovieRealmObject::class.java
        private val CLAZZ_GENRE = GenreRealmObject::class.java
    }

    override fun fetch(): Observable<Movie> {
        realm().use {
            val favorites = it.copyFromRealm(it.where(CLAZZ_MOVIE).findAllSorted("favoredAt", Sort.ASCENDING))
            return Observable.fromIterable(favorites.map(MovieRealmObject::toMovie))
        }
    }

    override fun favorite(movie: Movie): Observable<Movie> {
        var favorite: MovieRealmObject? = null

        movie.apply {
            val list = RealmList<GenreRealmObject>()
            genres?.forEach { list.add(GenreRealmObject(it.id, it.name)) }

            favorite = MovieRealmObject(id, Date(), releaseDate, voteAverage,
                    title, popularity, backDropPath, adult, list, overview)
        }

        realm().use {
            it.executeTransaction {
                it.copyToRealm(favorite)
            }
        }

        return Observable.just(favorite?.toMovie())
    }

    override fun deleteAll() =
            realm().use {
                it.executeTransaction { realm ->
                    realm.delete(CLAZZ_MOVIE)
                    realm.delete(CLAZZ_GENRE)
                }
            }

    override fun unFavorite(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}