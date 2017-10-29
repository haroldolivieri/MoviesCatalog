package haroldolivieri.moviescatalog

import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.movies.MoviesView
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import haroldolivieri.moviescatalog.repository.remote.entities.MovieRemote
import org.mockito.Mockito.mock
import org.powermock.api.support.membermodification.MemberModifier
import java.util.*


fun callPrivateMethod(instance: Any, methodName: String, parameter: Array<Any?>): Any? {
    val method = MemberModifier.method(instance::class.java, methodName)
    method.isAccessible = true
    return method.invoke(instance, *parameter)
}

val movieViewMock: MoviesView = mock(MoviesView::class.java)
val favoriteRepositoryMock: FavoritesRepository = mock(FavoritesRepository::class.java)

val favoredMovies = listOf(Movie(1, true, 0.0, "", 1.0, "", false,
        listOf(Genre(1, "Thriller"), Genre(2, "Action")), Date(), ""))

val genres = listOf(Genre(1, "Thriller"), Genre(2, "Action"), Genre(3, "Drama"))

val date = Date()
val moviePage1 = listOf(
        MovieRemote(1, 0.0, "title1", 1.0, "", false, listOf(1, 2), date, ""),
        MovieRemote(2, 0.0, "title2", 1.0, "", false, listOf(2, 3), date, ""),
        MovieRemote(3, 0.0, "title3", 1.0, "", false, listOf(1, 3), date, ""))

val moviePage1Matched = listOf(
        Movie(1, true, 0.0, "title1", 1.0, "http://image.tmdb.org/t/p/w500", false,
                listOf(Genre(1, "Thriller"), Genre(2, "Action")), date, ""),
        Movie(2, false, 0.0, "title2", 1.0, "http://image.tmdb.org/t/p/w500", false,
                listOf(Genre(2, "Action"), Genre(3, "Drama")), date, ""),
        Movie(3, false, 0.0, "title3", 1.0, "http://image.tmdb.org/t/p/w500", false,
                listOf(Genre(1, "Thriller"), Genre(3, "Drama")), date, ""))