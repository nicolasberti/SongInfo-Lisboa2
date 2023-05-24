package ayds.lisboa.songinfo.moredetails.data.internal

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardsLocalStorage {
    fun saveCard(card: Card.CardImpl)
    fun getCards(artist: String): List<Card>

}