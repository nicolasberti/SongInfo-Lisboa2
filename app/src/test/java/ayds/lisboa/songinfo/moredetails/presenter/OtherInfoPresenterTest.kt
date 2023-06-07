package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.presenter.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val cardRepository: CardRepository = mockk(relaxUnitFun = true)
    private val cardResolver: CardResolver = mockk(relaxUnitFun = true)

    private val otherInfoPresenter: OtherInfoPresenter by lazy {
        OtherInfoPresenterImpl(cardRepository, cardResolver)
    }

    @Test
    fun `should notify uistate on action search event`(){
        val artistName = "artist"
        val cards = listOf(
            Card("info1", "infoUrl1", Source.LastFM, "sourceLogoUrl1", false),
            Card("info2", "infoUrl2", Source.NYTimes, "sourceLogoUrl2", false),
            Card("info3", "infoUrl3", Source.Wikipedia, "sourceLogoUrl3", false)
        )
        val uiCards = listOf(
            UiCard("info1", "infoUrl1" , "Last FM", "sourceLogoUrl1"),
            UiCard("info2", "infoUrl2" , "New York Times", "sourceLogoUrl2"),
            UiCard("info3", "infoUrl3" , "Wikipedia", "sourceLogoUrl3")
        )

        every { cardRepository.getCards(artistName) } returns cards
        every { cardResolver.getSource(Source.LastFM) } returns "Last FM"
        every { cardResolver.getSource(Source.NYTimes) } returns "New York Times"
        every { cardResolver.getSource(Source.Wikipedia) } returns "Wikipedia"
        every { cardResolver.getFormattedInfo(cards[0], artistName) } returns "info1"
        every { cardResolver.getFormattedInfo(cards[1], artistName) } returns "info2"
        every { cardResolver.getFormattedInfo(cards[2], artistName) } returns "info3"

        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe{
            otherInfoUiStateTester(it)
        }

        otherInfoPresenter.actionSearch(artistName)

        val otherInfoUiStateExpected = OtherInfoUiState(uiCards)
        verify { otherInfoUiStateTester(otherInfoUiStateExpected) }
    }

}