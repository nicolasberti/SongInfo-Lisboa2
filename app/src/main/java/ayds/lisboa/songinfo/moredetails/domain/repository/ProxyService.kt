package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ProxyService{
    fun getCard(artist: String): Card
}

internal class LastFMProxy(
    private var artistService: ArtistService
) : ProxyService {
    override fun getCard(artist: String): Card {
        val artist = artistService.getArtist(artist)
        return if(artist != null)
            Card.CardImpl(artist.info, artist.url, "LastFM", artist.urlImageLastFM)
        else
            Card.EmptyCard
    }

}

internal class WikipediaProxy(
    //private var wikipediaService: WikipediaService // Se le debe injectar el servicio externo
) : ProxyService {
    override fun getCard(artist: String): Card {
        // Obtener del servicio externo y mapear a Card
        return Card.EmptyCard
    }

}

internal class NewYorkTimesProxy(
    //private var nytimesService: NewYorkTimesService // Se le debe injectar el servicio externo
) : ProxyService {
    override fun getCard(artist: String): Card {
        // Obtener del servicio externo y mapear a Card
        return Card.EmptyCard
    }

}