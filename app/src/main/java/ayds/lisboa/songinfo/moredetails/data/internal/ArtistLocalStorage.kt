package ayds.lisboa.songinfo.moredetails.data.internal

import ayds.lastfmservice.Artist

interface ArtistLocalStorage {

    fun saveArtist(artist: Artist.LastFMArtist)
    fun getArtist(artist: String): Artist.LastFMArtist?

}