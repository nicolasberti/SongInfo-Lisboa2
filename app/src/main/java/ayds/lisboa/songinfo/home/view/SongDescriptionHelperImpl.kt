package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {

     //private val songDescriptionHelper: SongDescriptionHelper = HomeViewInjector.songDescriptionHelper
     private val converter: Converter

    override fun getSongDescriptionText(song: Song): String {
        converter = ConverterInjector.initConverter(precision)
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${converter.convert(song.releaseDate)}"
            else -> "Song not found"
        }
    }
}


