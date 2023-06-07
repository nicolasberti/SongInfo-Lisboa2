package ayds.lisboa.songinfo.moredetails.data.internal

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardsLocalStorage {
    fun saveCard(artist: String, card: Card)
    fun getCards(artist: String): List<Card>

}