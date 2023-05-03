package ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist

interface ArtistRepository {
    fun getArtist(artist: String): Artist
}