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
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainPresenter {
    fun fetchPopularMoviesData(page: Int = 1)
    fun favoriteAction(checked: Boolean, movie: Movie)
    fun performMovieOrder(filterGenres: HashMap<Int, Boolean>)
    fun onCreate()
    fun onDestroy()
    fun onConnected()
}

class MainPresenterImpl
@Inject constructor(private val mainView: MainView,
                    private val moviesRepository: MoviesRepository,
                    private val favoritesRepository: FavoritesRepository) : MainPresenter {

    private var movies: MutableList<Movie>? = null
    private var genres: List<Genre>? = null
    private var disposable: Disposable? = null

    override fun onCreate() {
        disposable = favoritesRepository
                .getFavoredItemObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val position = movies?.indexOfFirst { movie -> movie.id == it.movieId }
                    movies?.get(position!!)?.favored = it.favored
                    performMovieOrder(mainView.getGenresToFilter())
                }

        mainView.showLoading()
        fetchPopularMoviesData()
    }

    override fun onDestroy() {
        if (!disposable?.isDisposed!!) {
            disposable?.dispose()
        }
    }

    override fun onConnected() {
        if (movies == null || movies?.size == 0) {
            mainView.showLoading()
            fetchPopularMoviesData(1)
        }
    }

    override fun favoriteAction(checked: Boolean, movie: Movie) {
        if (checked) {
            favoritesRepository.favorite(movie).subscribe({}, { t ->
                Log.e(this::class.java.simpleName, t.message)
            })
        } else {
            movie.id?.let { favoritesRepository.unfavorite(it) }
        }
    }

    override fun fetchPopularMoviesData(page: Int) {
        val movies = moviesRepository.getPopularMovies(page)
        val genres = moviesRepository.getGenres()

        Observable.zip(movies, genres,
                BiFunction<List<MovieRemote>, List<Genre>, List<Movie>> { remoteMovies, genres ->

                    if (this.genres == null) this.genres = genres

                    val tempMovies = remoteMovies.map { remoteMovie ->
                        remoteMovie.toMovie(genres)
                    }

                    reindexMovies(tempMovies)
                    matchWithFavoredMovies()
                    return@BiFunction this.movies!!

                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mainView.showGenres(this@MainPresenterImpl.genres)
                    performMovieOrder(mainView.getGenresToFilter())
                }, { mainView.showError(it) }, { mainView.hideLoading() })
    }

    override fun performMovieOrder(filterGenres: HashMap<Int, Boolean>) {

        val result = movies?.filter { movie ->
            val temp = movie.genres?.filter {
                filterGenres.containsKey(it.id)
            }

            return@filter temp?.size!! > 0
        }

        mainView.showMovies(result)
    }

    private fun reindexMovies(tempMovies: List<Movie>) {
        if (this.movies == null) {
            this.movies = tempMovies as MutableList<Movie>?
        } else {
            this.movies!!.addAll((tempMovies as MutableList<Movie>?)!!)
        }
    }

    private fun matchWithFavoredMovies() {
        val favorites = favoritesRepository.fetch().toList().toObservable()
        favorites.subscribe { favs ->
            this.movies?.map { movie ->
                movie.favored = favs.any { it.id == movie.id }
            }
        }
    }
}