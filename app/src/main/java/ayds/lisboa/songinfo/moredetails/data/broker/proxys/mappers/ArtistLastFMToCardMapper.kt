package ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers
import ayds.lastfmservice.Artist
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface ArtistLastFMToCardMapper{
    fun getCard(artist: Artist.LastFMArtist): Card
}

internal class ArtistLastFMToCardMapperImpl : ArtistLastFMToCardMapper{

    override fun getCard(artist: Artist.LastFMArtist): Card {
        return Card(artist.info, artist.url, Source.LastFM, artist.urlImageLastFM)
    }

}