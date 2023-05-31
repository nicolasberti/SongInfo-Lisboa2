package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ProxyService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
const val NYTIMES_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
internal class NewYorkTimesProxy(
    private var nytimesService: NYTimesArtistInfoService
) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = nytimesService.getArtistInfo(artist)
            if(artistObject != null)
                Card(artistObject.abstract, artistObject.url, Source.NYTimes, NYTIMES_LOGO_URL)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }

}