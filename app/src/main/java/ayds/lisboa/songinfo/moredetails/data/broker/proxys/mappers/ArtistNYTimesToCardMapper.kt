package ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.ny3.newyorktimes.external.data.entities.NYTArtistInfo

interface ArtistNYTimesToCardMapper{
    fun getCard(artist: NYTArtistInfo): Card
}

internal class ArtistNYTimesToCardMapperImpl : ArtistNYTimesToCardMapper{

    override fun getCard(artist: NYTArtistInfo): Card {
        return Card(artist.abstract, artist.url, Source.NYTimes, artist.nytLogoUrl)
    }

}