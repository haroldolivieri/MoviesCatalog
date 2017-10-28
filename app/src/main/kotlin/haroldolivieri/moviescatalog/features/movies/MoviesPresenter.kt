package haroldolivieri.moviescatalog.features.movies

import android.util.Log
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.remote.MoviesRepository
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import haroldolivieri.moviescatalog.repository.remote.entities.MovieRemote
import haroldolivieri.moviescatalog.repository.remote.entities.toMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


interface MainPresenter {
    fun fetchPopularMoviesData(page: Int = 1)
    fun favoriteAction(checked: Boolean, movie: Movie)
}

class MainPresenterImpl
@Inject constructor(private val mainView: MainView,
                    private val moviesRepository: MoviesRepository,
                    private val favoritesRepository: FavoritesRepository) : MainPresenter {

    override fun favoriteAction(checked: Boolean, movie: Movie) {
        if (checked) {
            favoritesRepository.favorite(movie).subscribe({}, {t ->
                Log.e(this::class.java.simpleName, t.message)
            })
        } else {
            movie.id?.let { favoritesRepository.unfavorite(it) }
        }
    }

    override fun fetchPopularMoviesData(page: Int) {
        val movies = moviesRepository.getPopularMovies(page)
        val genres = moviesRepository.getGenres()
        val favorites = favoritesRepository.fetch().toList().toObservable()

        Observable.zip(movies, genres, favorites,
                Function3<List<MovieRemote>, List<Genre>, List<Movie>,
                        List<Movie>> { remoteMovies, genres, favorites ->

                    return@Function3 remoteMovies.map { remoteMovie ->
                        remoteMovie.toMovie(genres, favorites)
                    }

                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainView.showLoading() }
                .subscribe({ mainView.showMovies(it) },
                        { mainView.showError(it) },
                        { mainView.hideLoading() })
    }
}