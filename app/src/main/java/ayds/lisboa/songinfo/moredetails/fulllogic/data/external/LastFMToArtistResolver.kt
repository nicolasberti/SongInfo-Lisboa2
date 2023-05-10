package ayds.lisboa.songinfo.moredetails.fulllogic.data.external

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMToArtistResolver {
    fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl?
}

private const val JSON_ARTIST = "artist"
private const val ARTIST_NAME = "name"
private const val ARTIST_BIO = "bio"
private const val ARTIST_BIO_CONTENT = "content"
private const val ARTIST_SOURCE = "url"

internal class JsonToArtistResolver : LastFMToArtistResolver {

    override fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl? {
        return serviceData?.getArtist()?.let { item ->
            Artist.ArtistImpl(
                item.getName(),
                item.getBioContent(),
                item.getUrl()
            )
        }
    }

    private fun String?.getArtist(): JsonObject {
        val jsonServiceData = Gson().fromJson(this, JsonObject::class.java)
        val jsonArtist = jsonServiceData[JSON_ARTIST]
        return jsonArtist.asJsonObject
    }

    private fun JsonObject.getBioContent(): String {
        val artistBio = this[ARTIST_BIO].asJsonObject
        val artistBioContent = artistBio[ARTIST_BIO_CONTENT]
        return artistBioContent.asString
    }

    private fun JsonObject.getUrl(): String {
        val artistUrl = this[ARTIST_SOURCE]
        return artistUrl.asString
    }

    private fun JsonObject.getName(): String {
        val artistName = this[ARTIST_NAME]
        return artistName.asString
    }


}