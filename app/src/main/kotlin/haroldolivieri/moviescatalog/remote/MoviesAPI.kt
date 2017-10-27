package haroldolivieri.moviescatalog.remote

import haroldolivieri.moviescatalog.remote.entities.GenreRemote
import haroldolivieri.moviescatalog.remote.entities.MovieRemote
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesAPI {
    @GET("/movie/top_rated")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page: Int) : Observable<List<MovieRemote>>

    @GET("/genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String) : Observable<List<GenreRemote>>
}