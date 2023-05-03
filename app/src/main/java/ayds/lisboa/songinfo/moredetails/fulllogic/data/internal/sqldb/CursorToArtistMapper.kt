package ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist

interface CursorToArtistMapper {

    fun mapCursorToList(cursor: Cursor): List<Artist.ArtistImpl>
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun mapCursorToList(cursor: Cursor): List<Artist.ArtistImpl> {
        val itemsOfCursor: MutableList<Artist.ArtistImpl> = ArrayList()
        while (cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN))
            val source = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            val artist = Artist.ArtistImpl(name, info, source)
            itemsOfCursor.add(artist)
        }
        cursor.close()
        return itemsOfCursor
    }

}