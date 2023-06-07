package ayds.lisboa.songinfo.moredetails.data.broker

import ayds.lisboa.songinfo.moredetails.data.broker.proxys.ProxyService
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardsBroker {
    fun getCardInfo(artist: String): List<Card>
}

internal class CardsBrokerImpl(
    private var proxyServices: List<ProxyService>
) : CardsBroker {
    override fun getCardInfo(artist: String): List<Card> {
        val cards = ArrayList<Card>()
        for (proxyService in proxyServices) {
            val card = proxyService.getCard(artist)
            if(card != null)
                cards.add(card)
        }
        return cards
    }
}