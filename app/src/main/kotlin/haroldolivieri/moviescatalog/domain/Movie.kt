package haroldolivieri.moviescatalog.domain

import haroldolivieri.moviescatalog.remote.entities.GenreRemote


data class Movie(val voteAverage: Double,
                 val title: String,
                 val popularity: Double,
                 val backDropPath: String,
                 val adult: Boolean,
                 val genres: List<GenreRemote>?,
                 val overview: String)