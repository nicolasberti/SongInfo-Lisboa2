package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaService

interface ProxyService{
    fun getCard(artist: String): Card
}

internal class LastFMProxy(
    private var artistService: ArtistService
) : ProxyService {
    override fun getCard(artist: String): Card {
        val artistObject = artistService.getArtist(artist)
        return if(artistObject != null)
            Card(artistObject.info, artistObject.url, Source.LastFM, artistObject.urlImageLastFM)
        else
            Card(source = Source.LastFM)
    }

}

internal class WikipediaProxy(
    private var wikipediaService: WikipediaService
) : ProxyService {

    private val WIKIPEDIA_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

    override fun getCard(artist: String): Card {
        val artistObject = wikipediaService.getArtist(artist)
        return if(artistObject != null)
            Card(artistObject.description, artistObject.wikipediaURL, Source.Wikipedia, WIKIPEDIA_LOGO_URL)
        else
            Card(source = Source.Wikipedia)
    }

}

internal class NewYorkTimesProxy(
    private var nytimesService: NYTimesArtistInfoService
) : ProxyService {

    private val NYTIMES_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

    override fun getCard(artist: String): Card {
        val artistObject = nytimesService.getArtistInfo(artist)
        return if(artistObject != null)
            Card(artistObject.abstract, artistObject.url, Source.NYTimes, NYTIMES_LOGO_URL)
        else
            Card(source = Source.NYTimes)
    }

}