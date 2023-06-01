package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ProxyService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService

internal class NewYorkTimesProxy(
    private var nytimesService: NYTimesArtistInfoService
) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = nytimesService.getArtistInfo(artist)
            if(artistObject != null)
                Card(artistObject.abstract, artistObject.url, Source.NYTimes, artistObject.nytLogoUrl)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }
}