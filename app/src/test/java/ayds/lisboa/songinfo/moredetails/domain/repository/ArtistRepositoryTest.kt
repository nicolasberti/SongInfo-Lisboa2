package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.ArtistService
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ArtistRepositoryTest {
    private val artistLocalStorage: ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val artistService: ArtistService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(artistLocalStorage, artistService)
    }

    @Test
    fun `given non existing artist by name should return empty artist`() {
        every { artistLocalStorage.getArtist("name") } returns null

        val result = artistRepository.getArtist("name")

        assertEquals(Artist.EmptyArtist, result)
    }

    @Test
    fun `given existing artist by name should return artist`() {
        val artist: Artist.LastFMArtist = mockk()
        every { artistLocalStorage.getArtist("name") } returns artist

        val result = artistRepository.getArtist("id")

        Assert.assertEquals(artist, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getArtist("name") } returns null
        every { artistService.getArtist("name") } throws mockk<Exception>()

        val result = artistRepository.getArtist("name")

        Assert.assertEquals(Artist.EmptyArtist, result)
    }

}