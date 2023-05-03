package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.internal.ArtistLocalStorage

interface ArtistRepository {
    fun getInfo(artist: String): Artist
}

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getInfo(artist: String): Artist {
        var internalArtist = artistLocalStorage.getArtist(artist)

        when {
            internalArtist != null -> markArtistAsLocal(internalArtist)
            else -> {
                try {
                    var externalArtist = lastFMService.getArtist(artist)

                    (externalArtist as? Artist)?.let {
                        {
                            artistLocalStorage.saveArtist(externalArtist)
                            internalArtist = externalArtist
                        }
                    }
                    } catch (e: Exception) {
                        internalArtist = null
                    }
                }
            }
            return internalArtist ?: Artist.EmptyArtist
        }


    private fun markArtistAsLocal(artist: Artist.ArtistImpl) {
        artist.isLocallyStored = true
    }
}