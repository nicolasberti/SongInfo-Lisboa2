package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lastfmservice.Artist
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToArtistMapper: CursorToArtistMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {
    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        URL_COLUMN,
        SOURCE_COLUMN
    )
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableArtists)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: Artist.LastFMArtist) {
        val dataBase = this.writableDatabase
        val values = createValuesOfArtist(artist.name, artist.info, artist.url)
        dataBase.insert(ARTIST_TABLE, null, values)
    }

    private fun createValuesOfArtist(artist: String, info: String, url: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist)
        values.put(INFO_COLUMN, info)
        values.put(URL_COLUMN, url)
        values.put(SOURCE_COLUMN, 1)
        return values
    }
    override fun getArtist(artist: String): Artist.LastFMArtist? {
        val cursor = getCursor(artist)
        val itemsOfCursor = cursorToArtistMapper.mapCursorToList(cursor)
        return itemsOfCursor.getOrNull(0)
    }

    private fun getCursor(artist: String): Cursor {
        val dataBase = this.readableDatabase
        return dataBase.query(
            ARTIST_TABLE,
            projection,
            "$ARTIST_COLUMN  = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN DESC"
        )
    }
}