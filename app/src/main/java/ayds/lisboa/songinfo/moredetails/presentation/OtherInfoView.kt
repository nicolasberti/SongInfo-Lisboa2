package ayds.lisboa.songinfo.moredetails.presentation

import ayds.observer.Observer
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.MoreDetailsInjector
import com.squareup.picasso.Picasso
import java.util.*
import ayds.lisboa.songinfo.moredetails.data.*
import ayds.lisboa.songinfo.moredetails.domain.*
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils

class OtherInfoView: AppCompatActivity(
){
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var textMoreDetails: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

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
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

    @Suppress("DEPRECATION")
    private fun setTextInfoView(info: String) {
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(IMAGE_LASTFM_LOGO)
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml(info)
        }
    }

    private fun subscribeEvents() {
        otherInfoPresenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState){
        setTextInfoView(uiState.info)
        updateListenerUrl(uiState.url)
    }

    private fun updateListenerUrl(url: String) {
        urlButton.setOnClickListener { navigationUtils.openExternalUrl(this, url) }
    }
}