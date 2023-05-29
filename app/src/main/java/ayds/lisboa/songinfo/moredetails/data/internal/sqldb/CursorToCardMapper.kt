package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface CursorToCardMapper {
    fun mapCursorToList(cursor: Cursor): List<Card>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun mapCursorToList(cursor: Cursor): List<Card> {
        val itemsOfCursor: MutableList<Card> = ArrayList()
        while (cursor.moveToNext()){
            val source = Source.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_COLUMN)))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(URL_COLUMN))
            val urlLogo = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_LOGO_COLUMN))
            val card = Card(description = info, infoUrl = url, source = source, sourceLogoUrl = urlLogo)
            itemsOfCursor.add(card)
        }
        cursor.close()
        return itemsOfCursor
    }

}