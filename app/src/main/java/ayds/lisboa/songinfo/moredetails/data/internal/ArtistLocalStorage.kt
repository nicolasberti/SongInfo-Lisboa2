package ayds.lisboa.songinfo.moredetails.data.internal

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist

interface ArtistLocalStorage {

    fun saveArtist(artist: Artist.LastFMArtist)
    fun getArtist(artist: String): Artist.LastFMArtist?

}