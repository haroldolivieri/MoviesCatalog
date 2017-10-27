package haroldolivieri.moviescatalog.features.main

import haroldolivieri.moviescatalog.di.ApiKey
import haroldolivieri.moviescatalog.remote.entities.GenreRemote
import haroldolivieri.moviescatalog.remote.entities.MovieRemote
import haroldolivieri.moviescatalog.remote.MoviesAPI
import io.reactivex.Observable
import javax.inject.Inject


interface MoviesRepository {
    fun getPopularMovies(page: Int): Observable<List<MovieRemote>>
    fun getGenres(): Observable<List<GenreRemote>>
}

class MoviesRepositoryImpl @Inject constructor(private val moviesAPI: MoviesAPI,
                                               @ApiKey private val apiKey: String) : MoviesRepository {

    override fun getGenres(): Observable<List<GenreRemote>> =
            moviesAPI.getGenres(apiKey)

    override fun getPopularMovies(page: Int): Observable<List<MovieRemote>> =
            moviesAPI.getPopularMovies(apiKey, page)
}