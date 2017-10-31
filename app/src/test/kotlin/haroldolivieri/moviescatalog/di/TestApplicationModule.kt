package haroldolivieri.moviescatalog.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.TestFaker.Companion.fakerSubject
import haroldolivieri.moviescatalog.TestFaker.Companion.favoredMovies
import haroldolivieri.moviescatalog.TestFaker.Companion.moviePage1MatchedFaked
import haroldolivieri.moviescatalog.TestSchedulerProvider
import haroldolivieri.moviescatalog.di.qualifier.TestScheduler
import haroldolivieri.moviescatalog.repository.local.FavoredEvent
import haroldolivieri.moviescatalog.repository.local.FavoritesRepository
import io.reactivex.Observable
import org.mockito.Mockito.*
import javax.inject.Singleton

@Module
class TestApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideFavoriteRepository(): FavoritesRepository {
        val favoriteRepositoryMock: FavoritesRepository = mock(FavoritesRepository::class.java)

        `when`(favoriteRepositoryMock.fetch())
                .thenReturn(Observable.fromIterable(favoredMovies))
        `when`(favoriteRepositoryMock.favorite(moviePage1MatchedFaked[0]))
                .thenReturn(Observable.just(moviePage1MatchedFaked[0]))
        doNothing().`when`(favoriteRepositoryMock)
                .unfavorite(moviePage1MatchedFaked[0].id!!)

        return favoriteRepositoryMock
    }

    @Provides
    @Singleton
    fun provideFavoriteObservable(): Observable<FavoredEvent> = fakerSubject

    @Provides
    @TestScheduler
    @Singleton
    fun provideScheduler(): SchedulerProvider = TestSchedulerProvider()
}