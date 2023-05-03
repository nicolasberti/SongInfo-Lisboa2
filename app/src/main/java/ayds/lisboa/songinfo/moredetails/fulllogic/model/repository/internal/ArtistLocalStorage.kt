package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.internal

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist

interface ArtistLocalStorage {

    fun saveArtist(artist: Artist.ArtistImpl)
    fun getArtist(artist: String): Artist.ArtistImpl?

}