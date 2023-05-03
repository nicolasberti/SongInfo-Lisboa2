package ayds.lisboa.songinfo.moredetails.fulllogic.data.internal

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist

interface ArtistLocalStorage {

    fun saveArtist(artist: Artist.ArtistImpl)
    fun getArtist(artist: String): Artist.ArtistImpl?

}