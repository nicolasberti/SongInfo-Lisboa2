package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
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
            Card.CardImpl(artist, artistObject.info, artistObject.url, "LastFM", artistObject.urlImageLastFM)
        else
            Card.EmptyCard
    }

}

internal class WikipediaProxy(
    private var wikipediaService: WikipediaService
) : ProxyService {

    private val WIKIPEDIA_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

    override fun getCard(artist: String): Card {
        val artistObject = wikipediaService.getArtist(artist)
        return if(artistObject != null)
            Card.CardImpl(artist, artistObject.description, artistObject.wikipediaURL, "Wikipedia", WIKIPEDIA_LOGO_URL)
        else
            Card.EmptyCard
    }

}

internal class NewYorkTimesProxy(
    private var nytimesService: NYTimesArtistInfoService
) : ProxyService {

    private val NYTIMES_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

    override fun getCard(artist: String): Card {
        val artistObject = nytimesService.getArtistInfo(artist)
        return if(artistObject != null)
            Card.CardImpl(artist, artistObject.abstract, artistObject.url, "NewYork Times", NYTIMES_LOGO_URL)
        else
            Card.EmptyCard
    }

}