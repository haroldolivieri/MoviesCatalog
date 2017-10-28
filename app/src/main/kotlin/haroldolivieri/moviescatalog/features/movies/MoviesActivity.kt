package haroldolivieri.moviescatalog.features.movies

import android.os.Bundle
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.custom.EndlessRecyclerViewScrollListener
import haroldolivieri.moviescatalog.features.BaseActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import haroldolivieri.moviescatalog.domain.Movie
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showMovies(movies: List<Movie>?)
    fun showError(throwable: Throwable)
}

class MainActivity(override val layout: Int = R.layout.activity_main) : BaseActivity(), MainView {


    @Inject lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        mainPresenter.fetchPopularMoviesData()
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showMovies(movies: List<Movie>?) {
        Log.e(TAG, "ACHOU")
    }

    override fun showError(throwable: Throwable) {
        throwable.message?.let { showSnackBar(recyclerView, it) }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        val endLessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNextPageData(page)
            }
        }

        recyclerView.addOnScrollListener(endLessScrollListener)
    }

    private fun loadNextPageData(page: Int) {
        mainPresenter.fetchPopularMoviesData(page)
    }
}
