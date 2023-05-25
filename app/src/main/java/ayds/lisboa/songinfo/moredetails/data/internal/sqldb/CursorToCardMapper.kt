package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CursorToCardMapper {
    fun mapCursorToList(cursor: Cursor): List<Card.CardImpl>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun mapCursorToList(cursor: Cursor): List<Card.CardImpl> {
        val itemsOfCursor: MutableList<Card.CardImpl> = ArrayList()
        while (cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN))
            val source = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(URL_COLUMN))
            val card = Card.CardImpl(name, info, url, source)
            itemsOfCursor.add(card)
        }
        cursor.close()
        return itemsOfCursor
    }

}