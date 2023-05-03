package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.external

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist
import retrofit2.Response

internal class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtistResolver: LastFMToArtistResolver): LastFMService {

        override fun getArtist(artist: String): Artist.ArtistImpl{
            val callResponse = getSongFromService(artist)
            return lastFMToArtistResolver.getArtistFromExternalData(callResponse.body())
        }

        private fun getSongFromService(artistName: String?): Response<String> {
            return lastFMAPI.getArtistInfo(artistName).execute()
        }
}
