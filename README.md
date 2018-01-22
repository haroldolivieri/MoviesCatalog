<p align="center">
    <img src="moviescatalog.png" width="30%">
</p>

# MoviesCatalog

### An MVP App using Kotlin, Dagger-Android, RxJava2, Realm, Local cache with OkHttp, Mockito and Robolectric.

Top 50 movies from `moviedb API`.
This app counts with local data persistence with `Realm` and temporarily local cache with `OkHttp`. Data are loaded in an infinite scroll list and can be filtered by genre.


## Unit Tests & Test Coverage

I've used Robolectric to made my Activities and Presenters tests with no need to attach an Android Device or Emulator to it. I've created a test `dagger`'s component using the test environment, to create a mock of my dependency injection graph.

 `./gradlew testDebug jacocoTestReportDebug` runs unit tests with jacoco and creates a test coverage report (it can be found on MoviesCatalog/build/reports/jacoco/debug/index.html after running)

There are local persistence data tests in `/androidTest` folder because `Realm` just runs on a real or virtual Android device.

#### Roboelectric - Note for Linux and Mac Users

If you are on Linux or on a Mac, you will probably need to configure the default JUnit test runner configuration in order to work around a bug where Android Studio does not set the working directory to the module being tested. This can be accomplished by editing the run configurations, `Defaults -> JUnit` and changing the working directory value to `$MODULE_DIR$`. If you face any problem to run the tests, please, follow this [setup](http://robolectric.org/getting-started/) on **Building with Android Studio** section.

## Bonus Points
###### - The user should be able to save items as favourites, and these should be retained across app restarts, kill or catalog refresh. ✅  
To accomplish this task I've choose persist the favorite movies data with a `realm` database.

###### - Work offline. Ability to download content and use it while offline. We love offline! ✅  
To accomplish this task I've choose to persist the request data temporarily, through a local cache using `okhttp client` with `retrofit`. All requests are saved for 30 minutes and the app works with offline data during this time.

###### - The user should also be able to select one or more items and share the selection via ✅  
###### - Infinite scroll / Lazy load of more items. ✅  
###### - Add more filtering/sorting options (Year, Genre, keywords...) ✅  
###### - The ability to provide smooth transitions between screens and states ✅ 
