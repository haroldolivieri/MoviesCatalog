package haroldolivieri.moviescatalog

import android.os.Bundle
import haroldolivieri.moviescatalog.features.BaseActivity
import haroldolivieri.moviescatalog.features.main.MainView

class MainActivity(override val layout: Int = R.layout.activity_main) : BaseActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
