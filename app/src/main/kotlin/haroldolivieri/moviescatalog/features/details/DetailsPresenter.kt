package haroldolivieri.moviescatalog.features.details

import android.support.annotation.VisibleForTesting
import android.util.Log
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.repository.local.FavoredEvent
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


interface DetailsPresenter {
    fun favoriteAction(checked: Boolean, movie: Movie)
    @VisibleForTesting fun favoritesRepository() : FavoritesRepository
}

class DetailsPresenterImpl
@Inject constructor(private val detailsView: DetailsView,
                    private val favoritesRepository: FavoritesRepository,
                    private val publishSubject: PublishSubject<FavoredEvent>) : DetailsPresenter {

    override fun favoriteAction(checked: Boolean, movie: Movie) {
        if (checked) {
            favoritesRepository.favorite(movie).subscribe({
                publishSubject.onNext(FavoredEvent(true, movie.id!!))
            }, { t ->
                Log.e(this::class.java.simpleName, t.message)
            })
        } else {
            movie.id?.let { favoritesRepository.unfavorite(it) }
            publishSubject.onNext(FavoredEvent(false, movie.id!!))
        }
    }

    override fun favoritesRepository(): FavoritesRepository = favoritesRepository
}