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
        var releaseDatePrecision: String,
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
                    year + " " + calculadoraBiciesto.esBiciesto(year.toInt())
                }
                else -> ""
            }
    }

    object calculadoraBiciesto {

        fun esBiciesto(year: Int): String =
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
                "(leap year)"
            else
                "(not a leap year)"
    }

    object EmptySong : Song()

}

