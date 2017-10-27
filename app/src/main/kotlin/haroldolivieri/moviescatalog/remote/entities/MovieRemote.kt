package haroldolivieri.moviescatalog.remote.entities

import com.google.gson.annotations.SerializedName

//http://image.tmdb.org/t/p/w500
data class MovieRemote(@SerializedName("vote_average") val voteAverage: Double,
                       val title: String,
                       val popularity: Double,
                       @SerializedName("backdrop_path") val backDropPath: String,
                       val adult: Boolean,
                       @SerializedName("genre_ids") val genreIds: List<Int>,
                       val overview: String)