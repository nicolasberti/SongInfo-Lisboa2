package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardRepository {
    fun getCards(artist: String): List<Card>
}