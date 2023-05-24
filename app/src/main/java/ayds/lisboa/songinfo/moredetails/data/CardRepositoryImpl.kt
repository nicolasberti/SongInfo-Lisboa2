package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.Broker

class CardRepositoryImpl(
    private val cardsLocalStorage: CardsLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getArtist(artist: String): List<Card.CardImpl> {
        var cards = cardsLocalStorage.getCards(artist)
        when {
            !(cards.isEmpty()) -> markArtistAsLocal(cards)
            else -> {
                try {
                    val cardsBroker = broker.getCardInfo(artist)
                    for (card in cardsBroker) {
                        if(card != Card.EmptyCard)
                            cardsLocalStorage.saveCard(card)
                    }
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }
        return cards
    }



    private fun markArtistAsLocal(cards: List<Card.CardImpl>) {
        for(card in cards)
            card.isLocallyStored = true
    }
}