package ayds.lisboa.songinfo.moredetails.presentation

import CardAdapter
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.data.*
import ayds.lisboa.songinfo.moredetails.dependencyInjector.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.domain.*
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils
import ayds.observer.Observer
import com.squareup.picasso.Picasso
import java.util.*

class OtherInfoView: AppCompatActivity(
){
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var textMoreDetails: TextView
    private lateinit var textSource: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

    private lateinit var viewPager: ViewPager2
    private lateinit var cardItems: MutableList<Card>
    private lateinit var cardAdapter: CardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details)
        /*
        initModule()
        initProperties()
        subscribeEvents()
        searchAction()*/
        viewPager = findViewById(R.id.viewPager);

        cardItems = mutableListOf()
        val texto = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        cardItems.add(Card(texto,"https://www.google.com.ar", Source.LastFM,"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", true))
        cardItems.add(Card(texto,"https://www.facebook.com", Source.NYTimes,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU", true))
        cardItems.add(Card(texto,"https://www.gmail.com", Source.Wikipedia,"https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png", true))


        cardAdapter = CardAdapter(this, cardItems)
        viewPager.adapter = cardAdapter
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
        textSource = findViewById(R.id.textSource)
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

    private fun subscribeEvents() {
        otherInfoPresenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState){
        runOnUiThread {
            setImageView(uiState.lastFMCard.sourceLogoUrl)
            setTextInfoView(uiState.lastFMCard.description)
            updateListenerUrl(uiState.lastFMCard.infoUrl)
            setTextSourceView(uiState.lastFMCard.source.name)
        }
    }

    private fun setImageView(image: String){
        val picasso =  Picasso.get()
        val requestCreator = picasso.load(image)
        requestCreator.into(imageView)
    }

    @Suppress("DEPRECATION")
    private fun setTextInfoView(info: String) {
        textMoreDetails.text = Html.fromHtml(info)

    }

    @Suppress("DEPRECATION")
    private fun setTextSourceView(source: String) { // Método para cambiar el texto al "source" de moredetails
        textSource.text = Html.fromHtml(source)
    }

    private fun updateListenerUrl(url: String) {
        urlButton.setOnClickListener { navigationUtils.openExternalUrl(this, url) }
    }
}