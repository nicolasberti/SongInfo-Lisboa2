package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import ayds.lisboa.songinfo.home.controller.HomeController
import ayds.lisboa.songinfo.home.model.HomeModel
import ayds.lisboa.songinfo.home.view.HomeUiEvent
import ayds.lisboa.songinfo.home.view.HomeView
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.observer.Observer

interface Presenter {

    fun setOtherInfoWindow(otherInfoWindow: OtherInfoWindow)

    fun setArtistInfoRepository(artistInfoRepository: ArtistRepository)
}
internal class PresenterImpl: Presenter {

    private lateinit var otherInfoWindow: OtherInfoWindow
    private lateinit var artistInfoRepository: ArtistRepository

    fun getArtistInfo(artistName: String?): Artist? {
        return artistName?.let { artistInfoRepository.getArtist(it) }
    }

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
        val artistInfo = Artist.ArtistImpl("Nombre", "Info", "Source", true)
        otherInfoWindow.updateViewInfo(artistInfo);
    }

}