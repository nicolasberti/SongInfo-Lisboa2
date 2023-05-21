package ayds.lisboa.songinfo.moredetails.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.LastFMArtist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.EmptyArtist
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoResolver
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoResolverImpl
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
    fun `getFormattedInfo with LastFM artist and locally stored info returns formatted HTML`() {

        val artistInfo = LastFMArtist("Test artist", "Test bio","Test url",1,true)
        val artistName = "Test artist"

        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)


        val expected =  "<html><div width=400><font face=\"arial\">[*]Test bio</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with LastFM artist, not locally stored info returns formatted HTML`() {
        val artistInfo = LastFMArtist("Test artist", "Test bio","Test url",1,false)
        val artistName = "Test artist"

        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">Test bio</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with empty artist returns no results`() {
        val artistInfo = EmptyArtist
        val artistName = "Test empty artist"

        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        val expected = "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with LastFM artist, not locally stored info, and empty info returns formatted HTML`() {
        val artistInfo = LastFMArtist("Test artist", "","Test url",1,false)
        val artistName = "Test artist"

        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">No results</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getFormattedInfo with LastFM artist, info isLocallyStored and info is empty, returns formatted info in html`() {
        val artistInfo = LastFMArtist("Test artist", "","Test url",1,true)
        val artistName = "Test artist"

        val result = artistInfoResolver.getFormattedInfo(artistInfo, artistName)

        val expected =  "<html><div width=400><font face=\"arial\">[*]</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and artist is EmptyArtist, when getUrl is called, then returns no results`() {
        val artistInfo = EmptyArtist

        val result = artistInfoResolver.getUrl(artistInfo)

        val expected = "URL NOT FOUND"
        assertEquals(expected, result)
    }

    @Test
    fun `given artist info and artist is LastFMArtist, when getUrl is called, then returns url`() {
        val artistInfo = LastFMArtist("Test artist", "Test info","https://www.google.com.ar",1,true)

        val result = artistInfoResolver.getUrl(artistInfo)

        val expected = "https://www.google.com.ar"
        assertEquals(expected, result)
    }
}