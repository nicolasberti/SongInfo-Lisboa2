package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.content.Intent
import android.net.Uri
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.observer.Observer
import java.util.concurrent.CompletableFuture

interface Presenter {

    fun setOtherInfoWindow(otherInfoWindow: OtherInfoWindow)

    fun setArtistInfoRepository(artistInfoRepository: ArtistRepository)
}
internal class PresenterImpl: Presenter {

    private lateinit var otherInfoWindow: OtherInfoWindow
    private lateinit var artistInfoRepository: ArtistRepository

    override fun setOtherInfoWindow(otherInfoWindow: OtherInfoWindow) {
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
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(otherInfoWindow.uiState.url)
        //startActivity(intent)
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