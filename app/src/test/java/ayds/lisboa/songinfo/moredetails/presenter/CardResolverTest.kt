package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.presentation.presenter.CardResolver
import ayds.lisboa.songinfo.moredetails.presentation.presenter.CardResolverImpl
import ayds.lisboa.songinfo.moredetails.presentation.presenter.LabelFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class CardResolverTest {

    private val labelFactory: LabelFactory = mockk(relaxUnitFun = true)
    private lateinit var cardResolver: CardResolver

    @Before
    fun setUp() {
        cardResolver = CardResolverImpl(labelFactory)
    }
    @Test
    fun `getFormattedInfo with card and locally stored info returns formatted HTML`() {

        val card = Card("Test description", "Test url",Source.LastFM,"Test logo url",true)
        val artistName = "Test artist"

        val result = cardResolver.getFormattedInfo(card, artistName)


        val expected =  "<html><div width=400><font face=\"arial\">[*]Test description</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with card and not locally stored info returns formatted HTML`() {
        val card = Card("Test description", "Test url",Source.LastFM,"Test logo url",false)
        val artistName = "Test artist"

        val result = cardResolver.getFormattedInfo(card, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">Test description</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with card, not locally stored info, and empty description returns formatted HTML`() {
        val card = Card("", "Test url",Source.LastFM,"Test logo url",false)
        val artistName = "Test artist"

        val result = cardResolver.getFormattedInfo(card, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with card, isLocallyStored and info is empty, returns formatted info in html`() {
        val card = Card("", "Test url",Source.LastFM,"Test logo url",true)
        val artistName = "Test artist"

        val result = cardResolver.getFormattedInfo(card, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getSource with card source, returns string with corresponding label`() {
        val lastFMCard = Card("Test description", "Test url",Source.LastFM,"Test logo url",true)
        val nyTimesCard = Card("Test description", "Test url",Source.NYTimes,"Test logo url",true)
        val wikipediaCard = Card("Test description", "Test url",Source.Wikipedia,"Test logo url",true)

        every { cardResolver.getSource(Source.LastFM) } returns "Last FM"
        every { cardResolver.getSource(Source.NYTimes) } returns "New York Times"
        every { cardResolver.getSource(Source.Wikipedia) } returns "Wikipedia"

        val lastFMResult = cardResolver.getSource(lastFMCard.source)
        val nyTimesResult = cardResolver.getSource(nyTimesCard.source)
        val wikipediaResult = cardResolver.getSource(wikipediaCard.source)

        assertEquals("Last FM", lastFMResult)
        assertEquals("New York Times", nyTimesResult)
        assertEquals("Wikipedia", wikipediaResult)
    }
}