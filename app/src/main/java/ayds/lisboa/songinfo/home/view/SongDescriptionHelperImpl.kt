package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

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
                        "Date: ${getDate(song.releaseDate,song.releaseDatePrecision)}"
            else -> "Song not found"
        }
    }

    private fun getDate(releaseDate:String, releaseDatePrecision:String): String =
        when(releaseDatePrecision){
            "day" -> releaseDate
            "month" -> CalculatorYearMonth.converter(releaseDate)
            "year" -> { val year = releaseDate.split("-").first()
                year + " " + CalculatorLeap.itsLeap(year.toInt())
            }
            else -> "Date not found"
        }
    
    object CalculatorLeap {
        fun itsLeap(year: Int): String =
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
                "(leap year)"
            else
                "(not a leap year)"
    }

    object CalculatorYearMonth {
        @RequiresApi(Build.VERSION_CODES.O)
        fun converter(date: String): String {
            val month = date.split("-").get(1)
            val monthName = Month.of(month.toInt()).toString()
            val year = date.split("-").first()
            return "$monthName, $year"
        }
    }

}