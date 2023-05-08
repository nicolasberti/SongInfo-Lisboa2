package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository

abstract class Presenter {

    abstract val artistInfoRepository: ArtistRepository;

    fun getArtistInfo(artistName: String?): Artist? {
        return artistName?.let { artistInfoRepository.getArtist(it) }
    }

}