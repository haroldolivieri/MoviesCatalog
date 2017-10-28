package haroldolivieri.moviescatalog.features.movies

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.custom.EndlessRecyclerViewScrollListener
import haroldolivieri.moviescatalog.domain.Movie
import haroldolivieri.moviescatalog.features.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showMovies(movies: List<Movie>?)
    fun showError(throwable: Throwable)
}

class MoviesActivity(override val layout: Int = R.layout.activity_main) : BaseActivity(),
        MainView, NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var mainPresenter: MainPresenter
    val movieAdapter by lazy {
        MoviesAdapter(context = this@MoviesActivity,
                favClick = { checked, movie -> mainPresenter.favoriteAction(checked, movie) },
                itemClick = { _, _ -> })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()

        setupFilterNavigationDrawer()
        setupToolbar()

        mainPresenter.fetchPopularMoviesData()
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

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showMovies(movies: List<Movie>?) {
        movieAdapter.setMovies(movies)
    }

    override fun showError(throwable: Throwable) {
        throwable.message?.let { showSnackBar(recyclerView, it) }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        val endLessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNextPageData(page + 1)
            }
        }

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = movieAdapter
        recyclerView.setEmptyView(emptyView)
        recyclerView.addOnScrollListener(endLessScrollListener)
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
    }

    private fun loadNextPageData(page: Int) {
        mainPresenter.fetchPopularMoviesData(page)
    }
}
