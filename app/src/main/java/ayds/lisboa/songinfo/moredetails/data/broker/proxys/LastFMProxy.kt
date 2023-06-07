package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers.ArtistLastFMToCardMapper

internal class LastFMProxy(
    private var artistService: ArtistService,
    private var artistLastFMToCardMapper: ArtistLastFMToCardMapper
) : ProxyService {
    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = artistService.getArtist(artist)
            if(artistObject != null)
                artistLastFMToCardMapper.getCard(artistObject)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }
}