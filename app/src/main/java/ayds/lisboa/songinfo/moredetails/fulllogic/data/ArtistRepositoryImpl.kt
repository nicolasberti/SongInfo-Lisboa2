package ayds.lisboa.songinfo.moredetails.fulllogic.data

import android.util.Log
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.ArtistLocalStorage

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtist(artist: String): Artist {
        var artistInfo = artistLocalStorage.getArtist(artist)

        when {
            artistInfo != null -> markArtistAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = lastFMService.getArtist(artist) // Retorna null. Arreglar esto. Puede ser la API o el Injector.

                    artistInfo?.let {
                        artistLocalStorage.saveArtist(artistInfo)
                    }
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }
        return artistInfo ?: Artist.EmptyArtist
    }



    private fun markArtistAsLocal(artist: Artist.ArtistImpl) {
        artist.isLocallyStored = true
    }
}