package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ProxyService

internal class LastFMProxy(
    private var artistService: ArtistService
) : ProxyService {
    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = artistService.getArtist(artist)
            if(artistObject != null)
                Card(artistObject.info, artistObject.url, "LastFM", artistObject.urlImageLastFM)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }
}