package ayds.lisboa.songinfo.moredetails.presenter
/*
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import ayds.lisboa.songinfo.moredetails.presentation.presenter.CardResolver
import ayds.lisboa.songinfo.moredetails.presentation.presenter.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.presenter.OtherInfoUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
*/
class OtherInfoPresenterTest {
/*
    private val artistInfoRepository: CardRepository = mockk(relaxUnitFun = true)
    private val cardResolver: CardResolver = mockk(relaxUnitFun = true)

    private val otherInfoPresenter by lazy {
        OtherInfoPresenterImpl(cardResolver)
    }

    @Test
    fun `should notify uistate on action search event`(){
        val artistName = "artist"
        val artistInfo: Artist = mockk()
        val info = "formatted info"
        val url = "https://google.com.ar"

        every { artistInfoRepository.getArtist(artistName) } returns artistInfo
        every { cardResolver.getFormattedInfo(artistInfo, artistName) } returns info
        every { cardResolver.getUrl(artistInfo) } returns url

        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe{
            otherInfoUiStateTester(it)
        }

        otherInfoPresenter.actionSearch(artistName)

        val otherInfoUiStateExpected = OtherInfoUiState(info, url)
        verify { otherInfoUiStateTester(otherInfoUiStateExpected) }
    }*/

}