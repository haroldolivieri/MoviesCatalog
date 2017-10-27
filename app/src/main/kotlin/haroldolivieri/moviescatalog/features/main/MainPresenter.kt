package haroldolivieri.moviescatalog.features.main

import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.remote.entities.GenreRemote
import haroldolivieri.moviescatalog.remote.entities.MovieRemote
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


interface MainPresenter {
    fun fetchPopularMoviesData(page: Int = 1)
}

class MainPresenterImpl
@Inject constructor(private val mainView: MainView,
                    private val moviesRepository: MoviesRepository) : MainPresenter {

    override fun fetchPopularMoviesData(page: Int) {
        val movies = moviesRepository.getPopularMovies(page)
        val genres = moviesRepository.getGenres()

        Observable.zip(movies, genres, BiFunction<List<MovieRemote>,
                List<GenreRemote>, List<Movie>> { remoteMovies, genres ->
            return@BiFunction remoteMovies.map { remoteMovie -> remoteMovie.toMovie(genres) }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainView.showLoading() }
                .subscribe({ mainView.showMovies(it) },
                        { mainView.showError(it) },
                        { mainView.hideLoading() })
    }
}

fun MovieRemote.toMovie(genresRemotes: List<GenreRemote>): Movie {
    val gender = genreIds.map { genreId -> genresRemotes.first { it.id == genreId } }
    return Movie(voteAverage, title, popularity, backDropPath, adult, gender, overview)
}