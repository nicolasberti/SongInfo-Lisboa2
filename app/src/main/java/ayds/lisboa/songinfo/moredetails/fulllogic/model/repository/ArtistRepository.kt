package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist

interface ArtistRepository {
    fun saveArtist(artist: Artist.ArtistImpl)
    fun getInfo(artist: Artist.ArtistImpl): String
}

internal class ArtistRepositoryImpl(
) : ArtistRepository {

    override fun saveArtist(artist: Artist.ArtistImpl) {

    }

    override fun getInfo(artist: Artist.ArtistImpl): String {

    }
}