package haroldolivieri.moviescatalog.domain

import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import java.io.Serializable
import java.util.*


data class Movie(val id: Int?,
                 var favored: Boolean?,
                 val voteAverage: Double?,
                 val title: String?,
                 val popularity: Double?,
                 val backDropPath: String?,
                 val adult: Boolean?,
                 val genres: List<Genre>?,
                 val releaseDate: Date?,
                 val overview: String?) : Serializable