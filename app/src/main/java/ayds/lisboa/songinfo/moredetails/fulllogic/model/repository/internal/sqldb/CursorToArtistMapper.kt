package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.internal.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Artist

interface CursorToArtistMapper {

    fun mapCursorToList(cursor: Cursor): List<Artist.ArtistImpl>
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun mapCursorToList(cursor: Cursor): List<Artist.ArtistImpl> {
        val itemsOfCursor: MutableList<Artist.ArtistImpl> = ArrayList()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN))
            val source = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            val artist = Artist.ArtistImpl(id, name, info, source, false)
            itemsOfCursor.add(artist)
        }
        cursor.close()
        return itemsOfCursor
    }

}