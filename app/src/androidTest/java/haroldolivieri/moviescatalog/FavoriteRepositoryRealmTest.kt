package haroldolivieri.moviescatalog

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.favorites.FavoritesRepository
import haroldolivieri.moviescatalog.features.favorites.FavoritesRepositoryLocal
import haroldolivieri.moviescatalog.remote.entities.Genre
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import junit.framework.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.util.*

@RunWith(AndroidJUnit4::class)
class FavoriteRepositoryRealmTest {

    val realmConfiguration: RealmConfiguration by lazy {
        Realm.init(InstrumentationRegistry.getTargetContext())
        RealmConfiguration.Builder()
                .name("moviescatalog.realm.test")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
    }

    lateinit var favoriteRepository: FavoritesRepository

    @Before
    fun setUp() {
        favoriteRepository = FavoritesRepositoryLocal(realmConfiguration)
        favoriteRepository.deleteAll()
    }

    @Test
    fun testFavoriteMovieWithSuccess() {
        val genres = listOf(Genre(1, "Thriller"), Genre(2, "Action"))
        val movieToBeFavored = Movie(211672, false, 6.4, "Minions", 618.378251,
                "/uX7LXnsC7bZJZjn048UCOwkPXWJ.jpg", false, genres, Date(),
                "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill," +
                        " a super-villain who, alongside her inventor husband Herb, " +
                        "hatches a plot to take over the world.")

        val result = favoriteRepository.favorite(movieToBeFavored)

        result.subscribe {
            Assert.assertEquals(true, it.favored)
        }
    }

    @Test
    fun testFavoriteManyMoviesWithSameCategory() {
        val genres = listOf(Genre(1, "Thriller"), Genre(2, "Action"))
        val movieToBeFavored1 = Movie(1, false, 6.4, "", 618.378251, "", false, genres, Date(), "")
        val movieToBeFavored2 = Movie(2, false, 6.4, "", 618.378251, "", false, genres, Date(), "")
        val movieToBeFavored3 = Movie(3, false, 6.4, "", 618.378251, "", false, genres, Date(), "")
        val movieToBeFavored4 = Movie(4, false, 6.4, "", 618.378251, "", false, genres, Date(), "")

        favoriteRepository.favorite(movieToBeFavored1)
        favoriteRepository.favorite(movieToBeFavored2)
        favoriteRepository.favorite(movieToBeFavored3)
        favoriteRepository.favorite(movieToBeFavored4)

        favoriteRepository.fetch()
                .toList()
                .toObservable()
                .subscribe {
                    Assert.assertEquals(it.size, 4)
                }
    }

    @Test(expected = RealmPrimaryKeyConstraintException::class)
    fun testAddManyTimesSameMovie() {
        var x = 5
        while (x > 0) {
            val genres = listOf(Genre(1, "Thriller"), Genre(2, "Action"))
            val movieToBeFavored = Movie(211672, false, 6.4, "Minions", 618.378251,
                    "/uX7LXnsC7bZJZjn048UCOwkPXWJ.jpg", false, genres, Date(),
                    "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill," +
                            " a super-villain who, alongside her inventor husband Herb, " +
                            "hatches a plot to take over the world.")

            val result = favoriteRepository.favorite(movieToBeFavored)
            x--
        }
    }

}
