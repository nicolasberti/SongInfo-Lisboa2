package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import ayds.lisboa.songinfo.moredetails.presentation.presenter.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class OtherInfoPresenterTest {

    private val labelFactory: LabelFactory = mockk(relaxUnitFun = true)
    private val cardRepository: CardRepository = mockk(relaxUnitFun = true)
    private val cardResolver: CardResolver by lazy {
        CardResolverImpl(labelFactory)
    }
    private val otherInfoPresenter: OtherInfoPresenter by lazy {
        OtherInfoPresenterImpl(cardRepository, cardResolver)
    }

    @Test
    fun `should notify uistate on action search event`(){
        val artistName = "artist"
        val card = mockk<Card>()
        val cards = mutableListOf<Card>()
        cards.add(card)
        val info = "formatted info"

        every { cardRepository.getCards(artistName) } returns cards
        every { cardResolver.getFormattedInfo(card, artistName) } returns info
        every { cardResolver.getSource(Source.LastFM) } returns "Last FM"
        every { cardResolver.getSource(Source.NYTimes) } returns "New York Times"
        every { cardResolver.getSource(Source.Wikipedia) } returns "Wikipedia"
        every { labelFactory.getLabelFromSource(Source.LastFM) } returns "Last FM"
        every { labelFactory.getLabelFromSource(Source.NYTimes) } returns "New York Times"
        every { labelFactory.getLabelFromSource(Source.Wikipedia) } returns "Wikipedia"

        val uiCards = listOf(
            UiCard(info, "infoUrl", "Last FM", "sourceLogo"),
            UiCard(info, "infoUrl", "New York Times", "sourceLogo"),
            UiCard(info, "infoUrl", "Wikipedia", "sourceLogo")
        )

        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe{
            otherInfoUiStateTester(it)
        }

        otherInfoPresenter.actionSearch(artistName)

        val otherInfoUiStateExpected = OtherInfoUiState(uiCards)
        verify { otherInfoUiStateTester(otherInfoUiStateExpected) }
    }

}