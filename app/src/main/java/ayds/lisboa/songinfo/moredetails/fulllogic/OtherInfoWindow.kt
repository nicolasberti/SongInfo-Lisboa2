package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private var dataBase: DataBase? = null
    private var textMoreDetails: TextView? = null
    private var lastFMAPI: LastFMAPI? = null
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

    private fun updateMoreDetails() {
        val intent = intent
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        initThreadInfo(artistName)
    }

    private fun initThreadInfo(artistName: String?) {
        val hilo = createThreadInfo(artistName)
        startThread(hilo)
    }

    private fun createThreadInfo(artistName: String?): Thread {
        return Thread {
            var info = getInfoDatabase(artistName)
            if (info == null) {
                val artist = getArtistAPI(artistName)
                val bio = getBioFromArtist(artist)
                val url = getURLFromArtist(artist)
                info = getInfoFromArtistAPI(bio, artistName)
                if (bio != null) saveInfo(artistName, info)
                setListenerURL(url)
            }
            setTextInfo(info)
        }
    }

    private fun saveInfo(artistName: String?, info: String?) {
        DataBase.saveArtist(dataBase, artistName, info)
    }

    private fun startThread(hilo: Thread) {
        hilo.start()
    }

    private fun getBioFromArtist(artist: JsonObject?): JsonElement {
        val bio = artist!!["bio"].asJsonObject
        return bio["content"]
    }

    private fun getURLFromArtist(artist: JsonObject?): String {
        val url = artist!!["url"]
        return url.asString
    }

    private fun setListenerURL(url: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun open() {
        dataBase = DataBase(this)
    }

    private fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        builder.append(text)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun formatOtherInfo(info: String, artist: String?): String {
        val textoSinComillas = info.replace("'", " ")
        val textoConSaltosDeLineaHTML = textoSinComillas.replace("\n", "<br>")
        val textoArtistaEnNegrita =
            textoConSaltosDeLineaHTML.replace("(?i)$artist".toRegex(), "<b>$artist</b>")
        return textoArtistaEnNegrita.replace(
            "(?i)$artist".toRegex(),
            artist!!.uppercase(Locale.getDefault())
        )
    }

    private fun setTextInfo(info: String?) {
        val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            textMoreDetails!!.text = Html.fromHtml(info)
        }
    }

    private fun getArtistAPI(artistName: String?): JsonObject? {
        val callResponse: Response<String>
        var artist: JsonObject? = null
        try {
            val call = lastFMAPI!!.getArtistInfo(artistName)
            callResponse = call.execute()
            val gson = Gson()
            val body = callResponse.body()
            val jobjBody = gson.fromJson(body, JsonObject::class.java)
            val jobjArtist = jobjBody["artist"]
            artist = jobjArtist.asJsonObject
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return artist
    }

    private fun getInfoFromArtistAPI(extract: JsonElement?, artistName: String?): String {
        var info = "No Results"
        if (extract != null) {
            info = extract.asString.replace("\\n", "\n")
            val otherInfoFormated = formatOtherInfo(info, artistName)
            info = textToHtml(otherInfoFormated)
        }
        return info
    }

    private fun getInfoDatabase(artistName: String?): String? {
        var info = DataBase.getInfo(dataBase, artistName)
        if (info != null) info = "[*]$info"
        return info
    }

    private fun createFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}