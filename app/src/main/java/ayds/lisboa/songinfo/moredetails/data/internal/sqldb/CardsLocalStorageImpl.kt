package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

internal class CardsLocalStorageImpl(
    context: Context,
    private val cursorToCardMapper: CursorToCardMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardsLocalStorage {
    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        URL_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_COLUMN
    )
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableArtists)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveCard(artist: String, card: Card) {
        val dataBase = this.writableDatabase
        val values = createValuesOfArtist(artist, card.description, card.infoUrl, card.source, card.sourceLogoUrl)
        dataBase.insert(ARTIST_TABLE, null, values)
    }

    private fun createValuesOfArtist(name:String, description: String, infoUrl: String, source: Source, sourceLogoUrl: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, name)
        values.put(INFO_COLUMN, description)
        values.put(URL_COLUMN, infoUrl)
        values.put(SOURCE_COLUMN, source.name)
        values.put(SOURCE_LOGO_COLUMN, sourceLogoUrl)
        return values
    }
    override fun getCards(artist: String): List<Card> {
        val cursor = getCursor(artist)
        return cursorToCardMapper.mapCursorToList(cursor)
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