package haroldolivieri.moviescatalog.features.main

import javax.inject.Inject


interface MainPresenter {

}

class MainPresenterImpl
@Inject constructor(private val mainView: MainView) : MainPresenter {


}