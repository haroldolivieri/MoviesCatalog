package haroldolivieri.moviescatalog.features.movies

import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.favorites.FavoritesRepository
import haroldolivieri.moviescatalog.remote.entities.Genre
import haroldolivieri.moviescatalog.remote.entities.MovieRemote
import haroldolivieri.moviescatalog.remote.entities.toMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


interface MainPresenter {
    fun fetchPopularMoviesData(page: Int = 1)
}

class MainPresenterImpl
@Inject constructor(private val mainView: MainView,
                    private val moviesRepository: MoviesRepository,
                    private val favoritesRepository: FavoritesRepository) : MainPresenter {

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