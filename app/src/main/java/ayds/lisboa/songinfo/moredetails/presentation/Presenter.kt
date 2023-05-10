package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observer
import java.util.concurrent.CompletableFuture

interface Presenter {

    fun setOtherInfoWindow(otherInfoWindow: OtherInfoView)

    fun setArtistInfoRepository(artistInfoRepository: ArtistRepository)
}
internal class PresenterImpl: Presenter {

    private lateinit var otherInfoWindow: OtherInfoView
    private lateinit var artistInfoRepository: ArtistRepository

    override fun setOtherInfoWindow(otherInfoWindow: OtherInfoView) {
        this.otherInfoWindow = otherInfoWindow
        otherInfoWindow.uiEventObservable.subscribe(observer)
    }

    override fun setArtistInfoRepository(artistInfoRepository: ArtistRepository) {
        this.artistInfoRepository = artistInfoRepository
    }

    private val observer: Observer<OtherInfoUiEvent> =
        Observer { value ->
            when (value) {
                OtherInfoUiEvent.OpenInfoUrl -> openInfoUrl()
                OtherInfoUiEvent.GetInfo -> getInfo()
            }
        }

    private fun openInfoUrl(){
        otherInfoWindow.openExternalLink(otherInfoWindow.uiState.url)
    }

    private fun getInfo(){
        val artistInfo = getArtistInfo(otherInfoWindow.uiState.searchTerm)
        otherInfoWindow.updateViewInfo(artistInfo);
    }


    private fun getArtistInfo(artistName: String): Artist {
        val future: CompletableFuture<Artist> = CompletableFuture.supplyAsync {
            artistInfoRepository.getArtist(artistName)
        }
        return future.get()
    }



}