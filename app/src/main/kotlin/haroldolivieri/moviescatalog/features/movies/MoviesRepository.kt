package haroldolivieri.moviescatalog.features.movies

import haroldolivieri.moviescatalog.di.ApiKey
import haroldolivieri.moviescatalog.remote.entities.Genre
import haroldolivieri.moviescatalog.remote.entities.MovieRemote
import haroldolivieri.moviescatalog.remote.MoviesAPI
import io.reactivex.Observable
import javax.inject.Inject


interface MoviesRepository {
    fun getPopularMovies(page: Int): Observable<List<MovieRemote>>
    fun getGenres(): Observable<List<Genre>>
}

class MoviesRepositoryRemote @Inject constructor(private val moviesAPI: MoviesAPI,
                                                 @ApiKey private val apiKey: String) : MoviesRepository {

    override fun getGenres(): Observable<List<Genre>> =
            moviesAPI.getGenres(apiKey)

    override fun getPopularMovies(page: Int): Observable<List<MovieRemote>> =
            moviesAPI.getPopularMovies(apiKey, page)
}