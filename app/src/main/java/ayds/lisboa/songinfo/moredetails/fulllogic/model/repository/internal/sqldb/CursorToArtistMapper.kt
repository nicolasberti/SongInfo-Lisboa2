package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.internal.sqldb

import android.database.Cursor

interface CursorToArtistMapper {

    fun mapCursorToList(cursor: Cursor): List<String>
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun mapCursorToList(cursor: Cursor): List<String> {
        val itemsOfCursor: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            itemsOfCursor.add(info)
        }
        cursor.close()
        return itemsOfCursor
    }

}