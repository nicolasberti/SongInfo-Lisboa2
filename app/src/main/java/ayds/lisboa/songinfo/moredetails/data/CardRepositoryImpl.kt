package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

internal class CardRepositoryImpl(
    private val cardsLocalStorage: CardsLocalStorage,
    private val cardsBroker: CardsBroker
) : CardRepository {

    override fun getCards(artist: String): List<Card> {
        var cards = cardsLocalStorage.getCards(artist)
        when {
            cards.isNotEmpty() -> markCardsAsLocal(cards)
            else -> {
                    cards = cardsBroker.getCardInfo(artist)
                    for (card in cards) {
                        cardsLocalStorage.saveCard(artist, card)
                    }
            }
        }
        return cards
    }

    private fun markCardsAsLocal(cards: List<Card>) {
        for(card in cards)
            card.isLocallyStored = true
    }
}