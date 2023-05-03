package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.*
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.*

class OtherInfoWindow : AppCompatActivity(
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

    private lateinit var dataBase: DataBase
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var textMoreDetails: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        createLastFMAPI()
        initDataBase()
        initThreadInfo()
    }

    private fun initProperties(){
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

    private fun createLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL_LASTFMAPI)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun initThreadInfo() {
        var artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName = artistName.toString()
        runThreadInfo(artistName)
    }

    private fun runThreadInfo(artistName: String) {
        Thread {
            updateMoreDetails(artistName)
        }.start()
    }

    private fun updateMoreDetails(artistName: String){
        val artistInfo = getArtistInfo(artistName)
        updateView(artistInfo)
    }

    private fun getArtistInfo(artistName : String): ArtistInfo{
        var artistInfo = getArtistBioFromDatabase(artistName)
        if (artistInfo.info != null)
            markArtistInfoAsLocalStored(artistInfo)
        else {
            artistInfo = getArtistBioFromAPI(artistName)
            if (artistInfo.info != null)
                saveBio(artistName, artistInfo)
        }
        return artistInfo
    }

    private fun updateView(artistInfo: ArtistInfo){
        updateViewUrl(artistInfo)
        updateViewInfo(artistInfo)
    }

    private fun updateViewUrl(artistInfo: ArtistInfo){
        val url = artistInfo.url
        url?.let { setListenerURL(it) }
    }

    private fun updateViewInfo(artistInfo: ArtistInfo){
        val info = getInfoFromArtistInfo(artistInfo)
        textToHtml(info)
        setTextInfoView(info)
    }

    private fun getInfoFromArtistInfo(artistInfo: ArtistInfo): String{
        var info = artistInfo.info
        if (artistInfo.isLocallyStored)
            info = PREFIX_LOCALLY_STORED+"$info"
        else if (info == null)
            info = NO_RESULTS
        return info
    }

    private fun getArtistBioFromAPI(artistName: String): ArtistInfo {
        val artist = getArtistAPI(artistName)
        val bio = getBioFromArtist(artist)
        val info = formatInfo(bio, artistName)
        val url = getURLFromArtist(artist)
        return ArtistInfo(url, info, false)
    }

    private fun markArtistInfoAsLocalStored(artistInfo: ArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun getArtistBioFromDatabase(artistName: String): ArtistInfo {
        val info = dataBase.getInfo(artistName)
        return ArtistInfo(null, info, info != null)
    }

    private fun getArtistAPI(artistName: String?): JsonObject? {
        val callResponse = getSongFromService(artistName)
        return getSongFromExternalData(callResponse.body())
    }

    private fun getSongFromExternalData(serviceData: String?): JsonObject? {
        val gson = Gson()
        val jobjBody = gson.fromJson(serviceData, JsonObject::class.java)
        val jobjArtist = jobjBody[JSON_ARTIST]
        return jobjArtist.asJsonObject
    }

    private fun getSongFromService(artistName: String?): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

    private fun getBioFromArtist(artist: JsonObject?): String? {
        val bio = artist?.get(JSON_BIO)
        val bioJson = bio?.asJsonObject
        val content = bioJson?.get(JSON_CONTENT)
        return content?.asString
    }

    private fun getURLFromArtist(artist: JsonObject?): String? =
        artist?.let { it[JSON_URL].asString }

    private fun saveBio(artistName: String, artistInfo: ArtistInfo) {
        val bio = artistInfo.info
        bio?.let{ dataBase.saveArtist(artistName, bio) }
    }

    private fun setListenerURL(url: String) {
        urlButton.setOnClickListener {
            listenerURL(url)
        }

    }

    private fun listenerURL(url: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun formatInfo(info: String?, artist: String): String? =
        info?.let {
                val textoSinComillas = info.replace("'", " ")
                val textoConSaltosDeLineaHTML = textoSinComillas.replace("\\n", "<br>")
                val textoArtistaEnNegrita = textoConSaltosDeLineaHTML.replace("(?i)$artist".toRegex(), "<b>$artist</b>")
                val artistUpperCase = artist.uppercase(Locale.getDefault())
                textoArtistaEnNegrita.replace("(?i)$artist".toRegex(), artistUpperCase)
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