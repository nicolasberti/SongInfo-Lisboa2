package ayds.lisboa.songinfo.moredetails.fulllogic.data

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.ArtistLocalStorage

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtist(artist: String): Artist {
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