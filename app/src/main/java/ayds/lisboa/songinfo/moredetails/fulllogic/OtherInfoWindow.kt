package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

@Suppress("DEPRECATION")
class OtherInfoWindow : AppCompatActivity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private var url: String? = null
    private var info: String? = null
    private lateinit var dataBase: DataBase
    private lateinit var textMoreDetails: TextView
    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        setTextMoreDetails()
        createFMAPI()
        open()
        updateMoreDetails()
    }

    private fun setTextMoreDetails() {
        textMoreDetails = findViewById(R.id.textMoreDetails)
    }

    private fun createFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun open() {
        dataBase = DataBase(this)
    }

    private fun updateMoreDetails() {
        var artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName = artistName.toString()
        initThreadInfo(artistName)
    }

    private fun initThreadInfo(artistName: String) {
        val thread = newThreadInfo(artistName)
        thread.start()
    }

    private fun newThreadInfo(artistName: String): Thread {
        return Thread {
            setInfo(artistName)
            updateView()
        }
    }

    private fun updateView(){
        url?.let { setListenerURL(it) }
        setTextInfoView(info)
    }

    private fun setInfo(artistName : String){
        this.info = getInfoDatabase(artistName)
        if (info == null) {
            val artist = getArtistAPI(artistName)
            val bio = getBioFromArtist(artist)
            setURLFromArtist(artist)
            this.info = getInfoFromArtistAPI(bio, artistName)
            if (bio != null)
                saveInfo(artistName, this.info!!)
        }
    }

    private fun getInfoDatabase(artistName: String): String? {
        var info = dataBase.getInfo(artistName)
        if (info != null)
            info = "[*]$info"
        return info
    }

    private fun getArtistAPI(artistName: String?): JsonObject? {
        val callResponse = getSongFromService(artistName)
        return getSongFromExternalData(callResponse.body())
    }

    private fun getSongFromExternalData(serviceData: String?): JsonObject? {
        val gson = Gson()
        val jobjBody = gson.fromJson(serviceData, JsonObject::class.java)
        val jobjArtist = jobjBody["artist"]
        return jobjArtist.asJsonObject
    }

    private fun getSongFromService(artistName: String?): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

    private fun getBioFromArtist(artist: JsonObject?): JsonElement? {
        val bio = artist!!["bio"].asJsonObject
        return bio["content"]
    }

    private fun setURLFromArtist(artist: JsonObject?){
        val url = artist!!["url"]
        this.url = url.asString
    }

    private fun getInfoFromArtistAPI(extract: JsonElement?, artistName: String): String {
        var info = "No Results"
        if (extract != null) {
            info = extract.asString
            info = info.replace("\\n", "\n")
            val otherInfoFormatted = formatOtherInfo(info, artistName)
            info = textToHtml(otherInfoFormatted)
        }
        return info
    }

    private fun saveInfo(artistName: String, info: String) {
        dataBase.saveArtist(artistName, info)
    }

    private fun setListenerURL(url: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun formatOtherInfo(info: String, artist: String): String {
        val textoSinComillas = info.replace("'", " ")
        val textoConSaltosDeLineaHTML = textoSinComillas.replace("\n", "<br>")
        val textoArtistaEnNegrita = textoConSaltosDeLineaHTML.replace("(?i)$artist".toRegex(), "<b>$artist</b>")
        val artistUpperCase = artist.uppercase(Locale.getDefault())
        return textoArtistaEnNegrita.replace("(?i)$artist".toRegex(), artistUpperCase)
    }

    private fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        builder.append(text)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun setTextInfoView(info: String?) {
        val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(imageUrl)
            val imageView = findViewById<View>(R.id.imageView) as ImageView
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml(info)
        }
    }
}