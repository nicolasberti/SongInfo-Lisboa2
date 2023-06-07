package ayds.lisboa.songinfo.moredetails.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.dependencyInjector.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.presentation.presenter.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.presentation.presenter.OtherInfoUiState
import ayds.lisboa.songinfo.moredetails.presentation.presenter.UiCard
import ayds.observer.Observer

class OtherInfoView: AppCompatActivity(
){
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var viewPager: ViewPager2
    private lateinit var cardAdapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        subscribeEvents()
        searchAction()
    }

    fun setPresenter(otherInfoPresenter: OtherInfoPresenter){
        this.otherInfoPresenter = otherInfoPresenter
    }

    private fun searchAction(){
        val artistName = getSearchTermState()
        otherInfoPresenter.actionSearch(artistName)
    }

    private fun getSearchTermState(): String{
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        return artistName.toString()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties(){
        viewPager = findViewById(R.id.viewPager)
    }

    private fun subscribeEvents() {
        otherInfoPresenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState){
        runOnUiThread {
            createAdapter(uiState.cards)
        }
    }

    private fun createAdapter(cards: List<UiCard>){
        cardAdapter = CardAdapter(this, cards)
        viewPager.adapter = cardAdapter
    }
}