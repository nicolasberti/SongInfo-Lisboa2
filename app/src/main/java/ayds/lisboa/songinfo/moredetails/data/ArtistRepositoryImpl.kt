package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.ArtistService
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val artistService: ArtistService
) : ArtistRepository {

    override fun getArtist(artist: String): Artist {
        var artistInfo = artistLocalStorage.getArtist(artist)
        when {
            artistInfo != null -> markArtistAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = artistService.getArtist(artist)
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



    private fun markArtistAsLocal(artist: Artist.LastFMArtist) {
        artist.isLocallyStored = true
    }
}