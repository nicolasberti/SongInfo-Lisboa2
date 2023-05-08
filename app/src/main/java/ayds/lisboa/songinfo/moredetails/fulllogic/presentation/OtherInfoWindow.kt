package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.home.view.HomeUiEvent
import ayds.lisboa.songinfo.home.view.HomeUiState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.*
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.*
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.observer.Observable
import ayds.observer.Subject

class OtherInfoWindow: AppCompatActivity(){
    companion object {

        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGEN_LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        const val URL_LASTFMAPI = "https://ws.audioscrobbler.com/2.0/"
        const val HTML_WIDTH = "<html><div width=400>"
        const val HTML_FONT = "<font face=\"arial\">"
        const val HTML_END = "</font></div></html>"
        const val JSON_ARTIST = "artist"
        const val JSON_CONTENT = "content"
        const val JSON_URL = "url"
        const val JSON_BIO = "bio"
        const val NO_RESULTS = "No results"
        const val PREFIX_LOCALLY_STORED = "[*]"
    }

    internal class ArtistInfo(
        internal var url: String? = null,
        internal var info: String? = null,
        internal var isLocallyStored: Boolean = false
    )

    private var presenter: Presenter = PresenterImpl()
    private lateinit var textMoreDetails: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        presenter.setOtherInfoWindow(this) //No iria aca
        initListeners()
        searchAction()
    }

    private fun initListeners(){
        urlButton.setOnClickListener{ notifyOpenSongAction() }
    }

    private fun searchAction(){
        updateSearchTermState()
        notifyGetInfoAction()
    }

    private fun updateSearchTermState(){
        var artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName = artistName.toString()
        uiState = uiState.copy(searchTerm = artistName)
    }

    private fun notifyOpenSongAction(){
        onActionSubject.notify(OtherInfoUiEvent.OpenInfoUrl)
    }
    private fun notifyGetInfoAction(){
        onActionSubject.notify(OtherInfoUiEvent.GetInfo)
    }

    private fun initProperties(){
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

    public fun updateViewInfo(artistInfo: Artist.ArtistImpl?){
        val info = getInfoFromArtistInfo(artistInfo)
        textToHtml(info)
        setTextInfoView(info)
    }

    private fun getInfoFromArtistInfo(artistInfo: Artist.ArtistImpl?): String{
        var info = artistInfo?.info
        if (artistInfo?.isLocallyStored == true)
            info = PREFIX_LOCALLY_STORED+"$info"
        else if (info == null)
            info = NO_RESULTS
        return info
    }

    private fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(text)
        builder.append(HTML_END)
        return builder.toString()
    }

    @Suppress("DEPRECATION")
    private fun setTextInfoView(info: String?) {
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(IMAGEN_LASTFM_LOGO)
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml(info)
        }
    }
}