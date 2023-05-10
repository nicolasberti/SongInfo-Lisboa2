package ayds.lisboa.songinfo.moredetails.presentation

import ayds.observer.Observer
import android.content.Intent
import android.net.Uri
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
        initListeners()
        searchAction()
    }

    fun setPresenter(otherInfoPresenter: OtherInfoPresenter){
         this.otherInfoPresenter = otherInfoPresenter
    }

    private fun initListeners(){
        urlButton.setOnClickListener{ otherInfoPresenter.accionUrl() }
    }

    private fun searchAction(){
        val artistName = getSearchTermState()
        otherInfoPresenter.accionSearch(artistName)
    }

    private fun getSearchTermState(): String{
        var artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
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
    private fun setTextInfoView() {
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(IMAGE_LASTFM_LOGO)
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml("info de prueba")// Traer de UI)
        }
    }

    fun openExternalLink() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.google.com.ar")// Traer de UI)
        startActivity(intent)
    }
    private fun subscribeEvents() {
        otherInfoPresenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiEvent> =
        Observer { value ->
            when (value) {
                OtherInfoUiEvent.UpdateViewUrl -> openExternalLink()
                OtherInfoUiEvent.UpdateViewInfo -> setTextInfoView()
            }
        }
}