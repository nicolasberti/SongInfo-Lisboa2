package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CardRepositoryTest {
    private val cardsLocalStorage: CardsLocalStorage = mockk(relaxUnitFun = true)
    private val cardsBroker: CardsBroker = mockk(relaxUnitFun = true)

    private val cardsRepository: CardRepository by lazy {
        CardRepositoryImpl(cardsLocalStorage, cardsBroker)
    }

    @Test
    fun `dado un artista que no existe en el almacenamiento local, se busca utilizando el broker y se encuentra`() {
        every { cardsLocalStorage.getCards("name") } returns emptyList<Card>()
        val source: Source = mockk(relaxUnitFun = true)
        val card = Card("description", "info", source, "sourceLogoUrl", false)

        every { cardsBroker.getCardInfo("name") } returns listOf(card)

        val result = cardsRepository.getCards("name")

        Assert.assertEquals(listOf(card), result)
    }

    @Test
    fun `given an existing artist the local storage will return at least one card of that artist`() {
        val source: Source = mockk(relaxUnitFun = true)
        val card = Card("description", "info", source, "sourceLogoUrl", false)

        every { cardsLocalStorage.getCards("name") } returns listOf(card)

        val result = cardsRepository.getCards("name")

        Assert.assertEquals(listOf(card), result)
        Assert.assertTrue(card.isLocallyStored)
    }

    @Test
    fun `dado un artista que no existe, el repositorio lo busca utilizando el broker y no lo encuentra`() {
        every { cardsLocalStorage.getCards("name") } returns emptyList<Card>()

        every { cardsBroker.getCardInfo("name") } returns emptyList<Card>()

        val result = cardsRepository.getCards("name")

        Assert.assertEquals(emptyList<Card>(), result)
    }

}