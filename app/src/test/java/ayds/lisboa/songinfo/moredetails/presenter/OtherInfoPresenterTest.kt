package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val artistInfoRepository: CardRepository = mockk(relaxUnitFun = true)
    private val artistInfoResolver: ArtistInfoResolver = mockk(relaxUnitFun = true)

    private val otherInfoPresenter by lazy {
        OtherInfoPresenterImpl(artistInfoRepository, artistInfoResolver)
    }

    @Test
    fun `should notify uistate on action search event`(){
        val artistName = "artist"
        val artistInfo: Artist = mockk()
        val info = "formatted info"
        val url = "https://google.com.ar"

        every { artistInfoRepository.getArtist(artistName) } returns artistInfo
        every { artistInfoResolver.getFormattedInfo(artistInfo, artistName) } returns info
        every { artistInfoResolver.getUrl(artistInfo) } returns url

        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe{
            otherInfoUiStateTester(it)
        }

        otherInfoPresenter.actionSearch(artistName)

        val otherInfoUiStateExpected = OtherInfoUiState(info, url)
        verify { otherInfoUiStateTester(otherInfoUiStateExpected) }
    }

}