package ayds.lisboa.songinfo.moredetails.fulllogic.data.external

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMToArtistResolver {
    fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl
}

private const val JSON_ARTIST = "artist"
private const val ARTIST_NAME = "name"
private const val ARTIST_SOURCE = "url"
private const val ARTIST_BIO = "bio"
private const val ARTIST_BIO_CONTENT = "content"

internal class JsonToArtistResolver : LastFMToArtistResolver {

    override fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl {
        val gson = Gson()
        val jobjBody = gson.fromJson(serviceData, JsonObject::class.java)
        val jobjArtist = jobjBody[JSON_ARTIST].asJsonObject
        val name = jobjArtist.get(ARTIST_NAME).asString
        val source = jobjArtist.get(ARTIST_SOURCE).asString
        val jobjBio = jobjArtist[ARTIST_BIO].asJsonObject
        val content = jobjBio.get(ARTIST_BIO_CONTENT).asString
        return Artist.ArtistImpl(name, content, source)
    }


}