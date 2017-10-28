package haroldolivieri.moviescatalog.domain

import haroldolivieri.moviescatalog.remote.entities.Genre


data class Movie(val id: Int?,
                 val favored: Boolean?,
                 val voteAverage: Double?,
                 val title: String?,
                 val popularity: Double?,
                 val backDropPath: String?,
                 val adult: Boolean?,
                 val genres: List<Genre>?,
                 val overview: String?)