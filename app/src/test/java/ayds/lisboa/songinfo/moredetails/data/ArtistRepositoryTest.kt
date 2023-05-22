package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.lastfmservice.external.ArtistService
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test
import java.io.IOException

class ArtistRepositoryTest {
    private val artistLocalStorage: ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val artistService: ayds.lastfmservice.external.ArtistService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(artistLocalStorage, artistService)
    }

    @Test
    fun `given non existing artist in local storage and external service by name should return empty artist`() {
        every { artistLocalStorage.getArtist("name") } returns null
        every { artistService.getArtist("name") } returns null

        val result = artistRepository.getArtist("name")

        assertEquals(Artist.EmptyArtist, result)
    }

    @Test
    fun `given existing artist by name should return artist`() {
        val artist = Artist.LastFMArtist(
            "name",
            "info",
            "url",
            1,
            false
        )
        every { artistLocalStorage.getArtist("name") } returns artist

        val result = artistRepository.getArtist("name")

        Assert.assertEquals(artist, result)
        Assert.assertTrue(artist.isLocallyStored)
    }



    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getArtist("name") } returns null
        every { artistService.getArtist("name") } throws IOException()

        val result = artistRepository.getArtist("name")

        Assert.assertEquals(Artist.EmptyArtist, result)
    }



    @Test
    fun `given non existing artist in local storage but existing in external service by name should return artist`() {
        val artist = Artist.LastFMArtist(
            "name",
            "info",
            "url",
            1,
            false
        )
        every { artistLocalStorage.getArtist("name") } returns null
        every { artistService.getArtist("name") } returns artist

        val result = artistRepository.getArtist("name")

        Assert.assertEquals(artist, result)
        Assert.assertFalse(artist.isLocallyStored)
        verify { artistLocalStorage.saveArtist(artist) }
    }

}