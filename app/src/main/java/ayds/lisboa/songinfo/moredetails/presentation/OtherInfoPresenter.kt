package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Observer
import ayds.observer.Subject
import java.util.concurrent.CompletableFuture

interface OtherInfoPresenter {


    val uiEventObservable: Observable<OtherInfoUiEvent>
    fun accionSearch(artistName: String)
    fun accionUrl()
}
internal class OtherInfoPresenterImpl(
    private var artistInfoRepository: ArtistRepository
    ): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    override val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    override fun accionUrl(){
        onActionSubject.notify(OtherInfoUiEvent.UpdateViewUrl)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun accionSearch(artistName: String){
        //val artistInfo = getArtistInfo(artistName)
        //val info = formatInfo(artistInfo)
        //updateUiStateInfo(info)
        onActionSubject.notify(OtherInfoUiEvent.UpdateViewInfo)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getArtistInfo(artistName: String): Artist {
        val future: CompletableFuture<Artist> = CompletableFuture.supplyAsync {
            artistInfoRepository.getArtist(artistName)
        }
        return future.get()
    }

    private fun updateUiStateInfo(informacion: String){
        uiState = uiState.copy(info = informacion)
    }

    fun formatInfo(artistInfo: Artist): String{
        val info = getInfoFromArtistInfo(artistInfo)
        return textToHtml(info)
    }

    companion object {
        const val HTML_WIDTH = "<html><div width=400>"
        const val HTML_FONT = "<font face=\"arial\">"
        const val HTML_END = "</font></div></html>"
        const val NO_RESULTS = "No results"
        const val PREFIX_LOCALLY_STORED = "[*]"
    }

    fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(text)
        builder.append(HTML_END)
        return builder.toString()
    }

    fun getInfoFromArtistInfo(artistInfo: Artist): String{
        return when (artistInfo){
            is Artist.EmptyArtist -> NO_RESULTS
            is Artist.LastFMArtist ->{
                var info = artistInfo?.info
                if (artistInfo?.isLocallyStored == true)
                    info = PREFIX_LOCALLY_STORED +"$info"
                else if (info == null)
                    info = NO_RESULTS
                info
            }
        }
    }

}