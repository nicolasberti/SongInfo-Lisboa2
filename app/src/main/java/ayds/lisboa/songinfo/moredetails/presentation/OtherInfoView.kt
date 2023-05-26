package ayds.lisboa.songinfo.moredetails.presentation

import ayds.observer.Observer
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.dependencyInjector.MoreDetailsInjector
import com.squareup.picasso.Picasso
import java.util.*
import ayds.lisboa.songinfo.moredetails.data.*
import ayds.lisboa.songinfo.moredetails.domain.*
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils

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