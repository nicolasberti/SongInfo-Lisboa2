package ayds.lisboa.songinfo.home.view

import Converter
import DateConverterFactory
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import io.mockk.mockk
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test

class SongDescriptionHelperTest {

    private val dateConverterFactory: DateConverterFactory = mockk(relaxUnitFun = true)
    private val dateConverter: Converter = mockk()
    private val songDescriptionHelper by lazy { SongDescriptionHelperImpl(dateConverterFactory) }

    @Test
    fun `given a local song it should return the description`() {
        every {dateConverterFactory.create("year")} returns dateConverter
        every{ dateConverter.getReleaseDate("1992-01-01")} returns "1992 (leap year)"

        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "year",
            "url",
            "url",
            true,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush [*]\n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Date: 1992 (leap year)"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local song it should return the description`() {
        every {dateConverterFactory.create("year")} returns dateConverter
        every{ dateConverter.getReleaseDate("1992-01-01")} returns "1992 (leap year)"

        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "year",
            "url",
            "url",
            false,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush \n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Date: 1992 (leap year)"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non spotify song it should return the song not found description`() {
        val song: Song = mockk()

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected = "Song not found"

        assertEquals(expected, result)
    }
}