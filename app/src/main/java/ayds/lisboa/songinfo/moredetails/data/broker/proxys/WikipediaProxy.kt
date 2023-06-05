package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers.ArtistWikipediaToCardMapper
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaService

internal class WikipediaProxy(
    private var wikipediaService: WikipediaService,
    private var artistWikipediaToCardMapper: ArtistWikipediaToCardMapper
) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = wikipediaService.getArtist(artist)
            if(artistObject != null)
                artistWikipediaToCardMapper.getCard(artistObject)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }
}