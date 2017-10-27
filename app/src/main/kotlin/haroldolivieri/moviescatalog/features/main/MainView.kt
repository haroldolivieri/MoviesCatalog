package haroldolivieri.moviescatalog.features.main

import haroldolivieri.moviescatalog.domain.Movie


interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showMovies(movies: List<Movie>?)
    fun showError(throwable: Throwable)
}