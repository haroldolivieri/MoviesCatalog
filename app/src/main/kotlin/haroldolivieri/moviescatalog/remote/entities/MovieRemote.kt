package haroldolivieri.moviescatalog.remote.entities

import com.google.gson.annotations.SerializedName
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.local.entities.MovieRealmObject


data class MovieRemote(val id: Int,
                       @SerializedName("vote_average") val voteAverage: Double,
                       val title: String,
                       val popularity: Double,
                       @SerializedName("backdrop_path") val backDropPath: String,
                       val adult: Boolean,
                       @SerializedName("genre_ids") val genreIds: List<Int>,
                       val overview: String)

fun MovieRemote.toMovie(genresRemotes: List<Genre>, favorites: List<Movie>): Movie {
    val gender = genreIds.map { genreId -> genresRemotes.first { it.id == genreId } }
    val favored = favorites.any { it.id == id }

    return Movie(id, favored, voteAverage, title, popularity,
            "http://image.tmdb.org/t/p/w500$backDropPath", adult, gender, overview)
}