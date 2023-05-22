package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lastfmservice.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoPresenter {

    val uiEventObservable: Observable<OtherInfoUiState>
    fun actionSearch(artistName: String)
}
internal class OtherInfoPresenterImpl(
    private var artistInfoRepository: ArtistRepository,
    private var artistInfoResolver: ArtistInfoResolver
): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    override fun actionSearch(artistName: String){
        Thread {
            threadActionSearch(artistName)
        }.start()
    }

    private fun threadActionSearch(artistName: String){
        val artistInfo = artistInfoRepository.getArtist(artistName)
        val uiState = getUiState(artistInfo, artistName)
        notifyState(uiState)
    }

    private fun getUiState(artistInfo: Artist, artistName: String): OtherInfoUiState{
        val info = artistInfoResolver.getFormattedInfo(artistInfo, artistName)
        val url = artistInfoResolver.getUrl(artistInfo)
        return OtherInfoUiState(info, url)
    }

    private fun notifyState(uiState: OtherInfoUiState){
        uiEventObservable.notify(uiState)
    }

}