package ayds.lisboa.songinfo.home.view

import Converter
import ConverterInjector
import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong


interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {

     //private val songDescriptionHelper: SongDescriptionHelper = HomeViewInjector.songDescriptionHelper
     private lateinit var converter: Converter

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> {
                ConverterInjector.initConverter(song.releaseDatePrecision)
                converter = ConverterInjector.getConverter()
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${converter.convert(song.releaseDate)}"
            }
            else -> "Song not found"
        }
    }
}


