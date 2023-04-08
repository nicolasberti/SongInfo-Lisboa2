package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${CalculatorDate.getDate(song)}"
            else -> "Song not found"
        }
    }
}

object CalculatorDate {
    fun getDate(song: SpotifySong): String =
        when(song.releaseDatePrecision){
            "day" -> ConverterDate.converterDate(song.releaseDate)
            "month" -> CalculatorYearMonth.converter(song.releaseDate)
            "year" -> { val year = song.releaseDate.split("-").first()
                year + " " + CalculatorLeap.itsLeap(year.toInt())
            }
            else -> "Date not found"
        }
}

object ConverterDate {
    fun converterDate(date: String): String {
        return date.replace("-", "/").split("/").reversed().joinToString(separator = "/")
    }
}

object CalculatorLeap {
    fun itsLeap(year: Int): String =
        if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
            "(leap year)"
        else
            "(not a leap year)"

}

object CalculatorYearMonth {
    fun converter(date: String): String {
        val month = date.split("-")[1].toInt() - 1
        val monthName = months()[month]
        val year = date.split("-").first()
        return "$monthName, $year"
    }

    fun months(): List<String>{
        val symbols = DateFormatSymbols()
        return symbols.months.filter { it.isNotEmpty() }
    }
}