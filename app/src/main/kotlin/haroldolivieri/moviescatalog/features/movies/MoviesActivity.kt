package haroldolivieri.moviescatalog.features.movies

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.custom.EndlessRecyclerViewScrollListener
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.BaseActivity
import haroldolivieri.moviescatalog.features.details.DetailsIntent
import haroldolivieri.moviescatalog.repository.remote.entities.Genre
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movies.*
import java.util.*
import javax.inject.Inject

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showMovies(movies: List<Movie>?)
    fun showError(throwable: Throwable)
    fun showMessage(message: String)
    fun showGenres(genres: List<Genre>?)
    fun getGenresToFilter(): HashMap<Int, Boolean>
}

class MoviesActivity(override val layout: Int = R.layout.activity_main) : BaseActivity(),
        MainView, NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var mainPresenter: MainPresenter

    val layoutManager = LinearLayoutManager(this)
    val endLessScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            if (totalItemsCount < 50) {
                loadNextPageData(page + 1)
            }
        }
    }

    val movieAdapter by lazy {
        MovieAdapter(context = this@MoviesActivity,
                favClick = { checked, movie -> mainPresenter.favoriteAction(checked, movie) },
                itemClick = { movie, options ->
                    startActivity(DetailsIntent(movie), options.toBundle())
                })
    }

    val genreAdapter by lazy {
        GenreAdapter(context = this@MoviesActivity,
                itemClick = { genres -> mainPresenter.performMovieOrder(filterGenres = genres) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclersView()

        setupFilterNavigationDrawer()
        setupToolbar()
        selectAllGenres.setOnClickListener { onFilterCleared() }

        mainPresenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_filter -> {
            drawerLayout.openDrawer(GravityCompat.END)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getGenresToFilter(): HashMap<Int, Boolean> = genreAdapter.getSelectedGenres()

    override fun showMessage(message: String) {
        showSnackBar(recyclerView, message)
    }

    override fun showMovies(movies: List<Movie>?) {
        movieAdapter.setMovies(movies)
    }

    override fun showGenres(genres: List<Genre>?) {
        genreAdapter.setGenres(genres)
    }

    override fun showError(throwable: Throwable) {
        Log.e(TAG, "error found -> ${throwable.localizedMessage}")
    }

    private fun onFilterCleared() {
        val selectedGenres = genreAdapter.getAllGenres()
        selectedGenres.forEach { i, _ -> selectedGenres.put(i, true) }
        genreAdapter.setSelectedGenres(selectedGenres)
        genreAdapter.notifyDataSetChanged()

        mainPresenter.performMovieOrder(selectedGenres)
    }

    private fun loadNextPageData(page: Int) {
        mainPresenter.fetchPopularMoviesData(page)
    }

    private fun setupRecyclersView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = movieAdapter
        recyclerView.setEmptyView(emptyView)
        recyclerView.addOnScrollListener(endLessScrollListener)

        categoriesList.adapter = genreAdapter
        categoriesList.layoutManager = StaggeredGridLayoutManager(2, 1)
    }

    private fun setupFilterNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
    }
}
