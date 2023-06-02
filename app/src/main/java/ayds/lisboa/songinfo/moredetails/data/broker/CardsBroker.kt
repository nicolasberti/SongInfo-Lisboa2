import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardsBroker {
    fun getCardInfo(artist: String): List<Card>
}

internal class CardsBrokerImpl(
    private var proxyServices: List<ProxyService>
) : CardsBroker {
    override fun getCardInfo(artist: String): List<Card> {
        var cards = ArrayList<Card>()
        for (proxyService in proxyServices) {
            val card = proxyService.getCard(artist)
            if(card != null)
                cards.add(card)
        }
        return cards
    }
}