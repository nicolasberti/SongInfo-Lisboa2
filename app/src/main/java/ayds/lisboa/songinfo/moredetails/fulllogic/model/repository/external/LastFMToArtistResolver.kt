package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.external


import ayds.lisboa.songinfo.moredetails.fulllogic.OtherInfoWindow
import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMToArtistResolver {
    fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl
}

internal class JsonToArtistResolver : LastFMToArtistResolver {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val JSON_ARTIST = "artist"
        const val JSON_CONTENT = "content"
        const val JSON_URL = "url"
        const val JSON_BIO = "bio"
    }


    override fun getArtistFromExternalData(serviceData: String?): Artist.ArtistImpl =
        try {
            serviceData?.getFirstItem()?.let { item ->
                Artist(
                    item.getId(),
                    item.getArtistAPI(),
                    item.getBioFromArtist(),
                    item.getAlbumName(),
                    item.formatInfo(bio, artistName),
                    item.getURLFromArtist(artist)
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)

        //val tracks = jobj[TRACKS].asJsonObject
        //val items = tracks[ITEMS].asJsonArray
        //return items[0].asJsonObject
    }

    private fun getBioFromArtist(artist: JsonObject?): String? {
        val bio = artist?.get(OtherInfoWindow.JSON_BIO)
        val bioJson = bio?.asJsonObject
        val content = bioJson?.get(OtherInfoWindow.JSON_CONTENT)
        return content?.asString
    }

    private fun getURLFromArtist(artist: JsonObject?): String? =
        artist?.let { it[OtherInfoWindow.JSON_URL].asString }

}