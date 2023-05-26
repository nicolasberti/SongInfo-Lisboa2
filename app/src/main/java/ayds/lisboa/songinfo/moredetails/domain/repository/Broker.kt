package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Broker {
    fun getCardInfo(artist: String): List<Card>
}

internal class BrokerImpl(
    private var proxyServices: List<ProxyService>
) : Broker {
    override fun getCardInfo(artist: String): List<Card> {
        var cards = ArrayList<Card>()
        for (proxyService in proxyServices) {
            cards.add(proxyService.getCard(artist))
        }
        return cards
    }

}