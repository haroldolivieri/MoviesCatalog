package haroldolivieri.moviescatalog.features

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import haroldolivieri.moviescatalog.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity : DaggerAppCompatActivity() {

    val TAG: String = this::class.java.simpleName
    abstract val layout: Int
    private var loadingContent: View? = null
    private var rootView: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun showLoading() {
        if (rootView == null) {
            createRootView()
        }

        if (loadingContent?.visibility != View.VISIBLE) {
            loadingContent?.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        if (loadingContent != null && loadingContent?.visibility == View.VISIBLE) {
            loadingContent?.visibility = View.GONE
        }
    }

    private fun createRootView() {
        val view = layoutInflater.inflate(R.layout.loading_screen, null)
        loadingContent = view.findViewById<FrameLayout>(R.id.parent_loading)
        rootView = window.decorView.rootView as ViewGroup
        rootView?.addView(loadingContent)
    }

    internal fun showSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackText = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        snackText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackBar.show()
    }
}