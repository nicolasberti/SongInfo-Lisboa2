package ayds.lisboa.songinfo.home.model.entities
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Month

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
        @RequiresApi(Build.VERSION_CODES.O)
        fun getDate(): String =
            when(releaseDatePrecision){
                "day" -> releaseDate
                "month" -> CalculatorYearMonth.converter(releaseDate)
                "year" -> { val year = releaseDate.split("-").first()
                    year + " " + CalculatorLeap.itsLeap(year.toInt())
                }
                else -> "Date not found"
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

