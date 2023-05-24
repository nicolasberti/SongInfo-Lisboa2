package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Broker {
    fun getCardInfo(artist: String): List<Card>
}

internal class BrokerImpl(
    private var proxyServices: List<ProxyService> // Se le deben injectar los 3 servicios
) : Broker {
    override fun getCardInfo(artist: String): List<Card> {

    }

}