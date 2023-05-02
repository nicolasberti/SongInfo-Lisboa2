package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.external

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist

interface ArtistService {
    fun getArtistInfo(artist: String): Artist
}