package ayds.lisboa.songinfo.home.model.entities
import java.text.SimpleDateFormat

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false,
        var releaseDatePrecision: String
    ) : Song(){
        fun getDate(): String =
            when(releaseDatePrecision){
                "day" -> releaseDate
                "month" -> { val outFormat = SimpleDateFormat("MMMM, yyyy", Locale.US)
                    outFormat.format(releaseDate)
                }
                "year" -> { val year = releaseDate.split("-").first()
                    year + " " + calculadoraBiciesto.esBiciesto(year.parseInt())
                }
            }
    }

    object calculadoraBiciesto {

        fun esBiciesto(year: Int): String {
            if (year % 4 == 0) {
                if (year % 100 == 0) {
                    year % 400 == 0
                } else {
                    return "(leap year)"
                }
            } else {
                return "(not a leap year)"
            }
        }
    }

    object EmptySong : Song()

}

