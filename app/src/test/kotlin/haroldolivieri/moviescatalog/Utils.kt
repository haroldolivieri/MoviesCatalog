package haroldolivieri.moviescatalog

import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.movies.MoviesView
import haroldolivieri.moviescatalog.repository.local.FavoredEvent
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import haroldolivieri.moviescatalog.repository.remote.entities.MovieRemote
import io.reactivex.subjects.PublishSubject
import org.mockito.Mockito.mock
import org.powermock.api.support.membermodification.MemberModifier
import java.util.*


fun callPrivateMethod(instance: Any, methodName: String, parameter: Array<Any?>): Any? {
    val method = MemberModifier.method(instance::class.java, methodName)
    method.isAccessible = true
    return method.invoke(instance, *parameter)
}

class TestFaker {
    companion object {

        val fakerSubject = PublishSubject.create<FavoredEvent>()!!

        val favoredMovies = listOf(Movie(1, 1, true, 0.0, "", 1.0, "", false,
                listOf(Genre(1, "Thriller"), Genre(2, "Action")), Date(), ""))

        val genresFaked = listOf(Genre(1, "Thriller"), Genre(2, "Action"), Genre(3, "Drama"))

        val fakerDate = Date()

        val moviePage1Faked = listOf(
                MovieRemote(1, 0.0, "title1", 1.0, "", false, listOf(1, 2), fakerDate, ""),
                MovieRemote(2, 0.0, "title2", 1.0, "", false, listOf(2, 3), fakerDate, ""),
                MovieRemote(3, 0.0, "title3", 1.0, "", false, listOf(1, 3), fakerDate, ""))

        val moviePage2Faked = listOf(
                MovieRemote(4, 0.0, "title4", 1.0, "", false, listOf(1, 2), fakerDate, ""),
                MovieRemote(5, 0.0, "title5", 1.0, "", false, listOf(2, 3), fakerDate, ""),
                MovieRemote(6, 0.0, "title6", 1.0, "", false, listOf(1, 3), fakerDate, ""))

        val moviePage1MatchedFaked = listOf(
                Movie(1, 1, true, 0.0, "title1", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(2, "Action")), fakerDate, ""),
                Movie(2, 2, false, 0.0, "title2", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(2, "Action"), Genre(3, "Drama")), fakerDate, ""),
                Movie(3, 3, false, 0.0, "title3", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(3, "Drama")), fakerDate, ""))

        val moviePage2MatchedFaked = listOf(
                Movie(4, 4, true, 0.0, "title4", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(2, "Action")), fakerDate, ""),
                Movie(5, 5, false, 0.0, "title5", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(2, "Action"), Genre(3, "Drama")), fakerDate, ""),
                Movie(6, 6, false, 0.0, "title6", 1.0, "http://image.tmdb.org/t/p/w500", false,
                        listOf(Genre(1, "Thriller"), Genre(3, "Drama")), fakerDate, ""))
    }
}