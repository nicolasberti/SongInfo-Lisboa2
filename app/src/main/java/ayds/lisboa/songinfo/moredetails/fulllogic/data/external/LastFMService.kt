package ayds.lisboa.songinfo.moredetails.fulllogic.data.external

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist

interface LastFMService {
    fun getArtist(artist: String): Artist.ArtistImpl?
}