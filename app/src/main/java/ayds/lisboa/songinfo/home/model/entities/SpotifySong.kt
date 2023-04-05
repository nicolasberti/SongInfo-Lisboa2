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
    ) : Song()

    object EmptySong : Song()
}

