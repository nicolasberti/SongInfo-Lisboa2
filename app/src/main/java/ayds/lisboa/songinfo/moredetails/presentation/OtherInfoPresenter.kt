package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject
import java.util.*
import java.util.concurrent.CompletableFuture

interface OtherInfoPresenter {

    val uiEventObservable: Observable<OtherInfoUiState>
    fun actionSearch(artistName: String)
}
internal class OtherInfoPresenterImpl(
    private var artistInfoRepository: ArtistRepository,
    private var artistInfoRetriever: ArtistInfoRetriever
): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    @RequiresApi(Build.VERSION_CODES.N)
    override fun actionSearch(artistName: String){
        val artistInfo = getArtistInfo(artistName)
        val uiState = getUiState(artistInfo, artistName)
        notifyState(uiState)
    }

    private fun getUiState(artistInfo: Artist, artistName: String): OtherInfoUiState{
        val info = artistInfoRetriever.getFormattedInfo(artistInfo, artistName)
        val url = artistInfoRetriever.getUrl(artistInfo)
        return OtherInfoUiState(info, url)
    }

    private fun notifyState(uiState: OtherInfoUiState){
        uiEventObservable.notify(uiState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getArtistInfo(artistName: String): Artist {
        val future: CompletableFuture<Artist> = CompletableFuture.supplyAsync {
            artistInfoRepository.getArtist(artistName)
        }
        return future.get()
    }

}