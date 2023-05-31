package ayds.lisboa.songinfo.moredetails.data

import Broker
import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

class CardRepositoryImpl(
    private val cardsLocalStorage: CardsLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getArtist(artist: String): List<Card> {
        var cards = cardsLocalStorage.getCards(artist)
        when {
            cards.isNotEmpty() -> markArtistAsLocal(cards)
            else -> {
                    cards = broker.getCardInfo(artist)
                    for (card in cards) {
                        cardsLocalStorage.saveCard(artist, card)
                    }
            }
        }
        return cards
    }

    private fun markArtistAsLocal(cards: List<Card>) {
        for(card in cards)
            card.isLocallyStored = true
    }
}