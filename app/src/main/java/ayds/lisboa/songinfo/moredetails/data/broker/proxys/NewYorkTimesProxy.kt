package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers.ArtistNYTimesToCardMapper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService

internal class NewYorkTimesProxy(
    private var nytimesService: NYTimesArtistInfoService,
    private var artistNYTimesToCardMapper: ArtistNYTimesToCardMapper
) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = nytimesService.getArtistInfo(artist)
            if(artistObject != null)
                artistNYTimesToCardMapper.getCard(artistObject)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }
}