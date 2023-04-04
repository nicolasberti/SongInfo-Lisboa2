package ayds.lisboa.songinfo.home.model.entities
import java.text.SimpleDateFormat
import java.util.*

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song(){
        fun getDate(): String =
            when(releaseDatePrecision){
                "day" -> releaseDate
                "month" -> { val outFormat = SimpleDateFormat("MMMM, yyyy", Locale.US)
                    outFormat.format(releaseDate)
                }
                "year" -> { val year = releaseDate.split("-").first()
                    year + " " + CalculatorLeap.itsLeap(year.toInt())
                }
                else -> ""
            }
    }

    object EmptySong : Song()

    object CalculatorLeap {
        fun itsLeap(year: Int): String =
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
                "(leap year)"
            else
                "(not a leap year)"

    }

}

