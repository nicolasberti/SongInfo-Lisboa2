
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ProxyService{
    fun getCard(artist: String): Card?
}




