package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lastfmservice.Artist

interface ArtistRepository {
    fun getArtist(artist: String): Artist
}