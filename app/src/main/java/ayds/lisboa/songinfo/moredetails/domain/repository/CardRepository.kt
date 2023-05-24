package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardRepository {
    fun getArtist(artist: String): List<Card>
}