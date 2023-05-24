package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CursorToArtistMapper {
    fun mapCursorToList(cursor: Cursor): List<Card.CardImpl>
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun mapCursorToList(cursor: Cursor): List<Card.CardImpl> {
        val itemsOfCursor: MutableList<Artist.LastFMArtist> = ArrayList()
        while (cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN))
            val source = cursor.getInt(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(URL_COLUMN))
            val artist = Artist.LastFMArtist(name, info, url, source)
            itemsOfCursor.add(artist)
        }
        cursor.close()
        return itemsOfCursor
    }

}