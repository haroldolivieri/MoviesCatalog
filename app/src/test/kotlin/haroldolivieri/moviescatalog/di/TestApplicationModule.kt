package haroldolivieri.moviescatalog.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import haroldolivieri.moviescatalog.TestSchedulerProvider
import haroldolivieri.moviescatalog.di.qualifier.TestScheduler
import haroldolivieri.moviescatalog.favoredMovies
import haroldolivieri.moviescatalog.favoriteRepositoryMock
import haroldolivieri.moviescatalog.moviePage1Matched
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
        `when`(favoriteRepositoryMock.fetch()).thenReturn(Observable.fromIterable(favoredMovies))
        `when`(favoriteRepositoryMock.favorite(moviePage1Matched[0])).thenReturn(Observable.just(moviePage1Matched[0]))
        doNothing().`when`(favoriteRepositoryMock).unfavorite(moviePage1Matched[0].id!!)

        `when`(favoriteRepositoryMock.getFavoredItemObservable()).thenReturn(Observable.just(FavoredEvent(false, 1)))
        return favoriteRepositoryMock
    }

    @Provides
    @TestScheduler
    @Singleton
    fun provideScheduler(): SchedulerProvider = TestSchedulerProvider()
}