package ayds.lisboa.songinfo.moredetails.fulllogic.presentation


import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.fulllogic.MoreDetailsInjector
import com.squareup.picasso.Picasso
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.*
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.*
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.observer.Observable
import ayds.observer.Subject

class OtherInfoWindow: AppCompatActivity(){
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

    private lateinit var textMoreDetails: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    private val formatterInfo = FormatterInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
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

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties(){
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

   fun updateViewInfo(artistInfo: Artist.ArtistImpl?){
        val info = formatterInfo.getInfoFromArtistInfo(artistInfo)
        formatterInfo.textToHtml(info)
        setTextInfoView(info)
    }

    @Suppress("DEPRECATION")
    private fun setTextInfoView(info: String?) {
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(IMAGE_LASTFM_LOGO)
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml(info)
        }
    }
}