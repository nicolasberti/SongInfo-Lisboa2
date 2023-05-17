package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.home.view.HomeUiEvent
import ayds.lisboa.songinfo.home.view.HomeUiState
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class OtherInfoPresenterTest {

    private val artistInfoRepository: ArtistRepository = mockk(relaxUnitFun = true)
    private val artistInfoResolver: ArtistInfoResolver = mockk(relaxUnitFun = true)

    private val otherInfoPresenter by lazy {
        OtherInfoPresenterImpl(artistInfoRepository, artistInfoResolver)
    }

    @Before
    fun setUp() {
        //otherInfoPresenter.uiEventObservable
    }

    @Test
    fun `should notify uistate on action search event`(){
        // Arrange
        val artistName = "artist"
        val artistInfo: Artist = mockk()
        val info = "formatted info"
        val url = "https://google.com.ar"
        val otherInfoUiState = OtherInfoUiState(info, url)

        every { artistInfoRepository.getArtist(artistName) } returns artistInfo
        every { artistInfoResolver.getFormattedInfo(artistInfo, artistName) } returns info
        every { artistInfoResolver.getUrl(artistInfo) } returns url

        // Act
        otherInfoPresenter.actionSearch(artistName)

        // Assert
        verify { otherInfoPresenter.notifyState(otherInfoUiState) }
    }

}