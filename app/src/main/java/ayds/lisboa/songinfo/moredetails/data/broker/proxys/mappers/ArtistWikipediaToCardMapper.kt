package ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.winchester3.wikiartist.artist.externalWikipedia.WIKIPEDIA_LOGO_URL
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaArtist

interface ArtistWikipediaToCardMapper{
    fun getCard(artist: WikipediaArtist): Card
}

internal class ArtistWikipediaToCardMapperImpl : ArtistWikipediaToCardMapper{

    override fun getCard(artist: WikipediaArtist): Card {
        return Card(artist.description, artist.wikipediaURL, Source.Wikipedia, WIKIPEDIA_LOGO_URL)

    }

}