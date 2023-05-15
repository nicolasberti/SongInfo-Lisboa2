package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.LastFMArtist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.EmptyArtist
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoResolver
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoResolverImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ArtistInfoResolverTest {

    private lateinit var artistInfoResolver: ArtistInfoResolver

    @Before
    fun setUp() {
        artistInfoResolver = ArtistInfoResolverImpl()
    }
    @Test
    fun `given artist info and name and artist is LastFMArtist and info isLocallyStored, when getFormattedInfo is called, then returns formatted info in html`() {
        // Arrange
        val artistInfo = LastFMArtist("Test artist", "Test bio","Test url",1,true)
        val artistName = "Test artist"

        // Act
        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        // Assert
        val expected =  "<html><div width=400><font face=\"arial\">[*]Test bio</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and name and artist is LastFMArtist and info not isLocallyStored, when getFormattedInfo is called, then returns formatted info in html`() {
        // Arrange
        val artistInfo = LastFMArtist("Test artist", "Test bio","Test url",1,false)
        val artistName = "Test artist"

        // Act
        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        // Assert
        val expected =  "<html><div width=400><font face=\"arial\">Test bio</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and name and artist is emptyArtist, when getFormattedInfo is called, then returns no results`() {
        // Arrange
        val artistInfo = EmptyArtist
        val artistName = "Test empty artist"

        // Act
        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        // Assert
        val expected = "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and name and artist is LastFMArtist and info not isLocallyStored and info is empty, when getFormattedInfo is called, then returns formatted info in html`() {
        // Arrange
        val artistInfo = LastFMArtist("Test artist", "","Test url",1,false)
        val artistName = "Test artist"

        // Act
        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        // Assert
        val expected =  "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and name and artist is LastFMArtist and info isLocallyStored and info is empty, when getFormattedInfo is called, then returns formatted info in html`() {
        // Arrange
        val artistInfo = LastFMArtist("Test artist", "","Test url",1,true)
        val artistName = "Test artist"

        // Act
        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        // Assert
        val expected =  "<html><div width=400><font face=\"arial\">[*]</font></div></html>"
        assertEquals(expected, result)
    }

}