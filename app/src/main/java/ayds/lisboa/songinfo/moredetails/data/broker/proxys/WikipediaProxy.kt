package ayds.lisboa.songinfo.moredetails.data.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ProxyService
import ayds.winchester3.wikiartist.artist.externalWikipedia.WIKIPEDIA_LOGO_URL
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaService
internal class WikipediaProxy(
    private var wikipediaService: WikipediaService
) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            val artistObject = wikipediaService.getArtist(artist)
            if(artistObject != null)
                Card(artistObject.description, artistObject.wikipediaURL, Source.Wikipedia, WIKIPEDIA_LOGO_URL)
            else
                null
        } catch (ioException: Exception) {
            null
        }
    }

}
