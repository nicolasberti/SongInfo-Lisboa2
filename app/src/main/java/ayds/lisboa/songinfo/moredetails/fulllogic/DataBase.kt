package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ARTIST_TABLE = "artist"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"

private const val createTableArtists: String =
    "create table $ARTIST_TABLE ("+
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "$ARTIST_COLUMN string, "+
            "$INFO_COLUMN string, "+
            "$SOURCE_COLUMN integer)"

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableArtists)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    /** -- Falta esto hacer ReFactor -- **/
    val URLConnection = "jdbc:sqlite:./dictionary.db"
    @Throws(SQLException::class)
    private fun createConnection(): Connection? {
        return DriverManager.getConnection(URLConnection)
    }

    @Throws(SQLException::class)
    private fun createStatement(connection: Connection?): Statement {
        val statement: Statement = connection.createStatement()
        statement.setQueryTimeout(30)
        return statement
    }

    @Throws(SQLException::class)
    private fun showArtists(statement: Statement) {
        val rs: ResultSet = statement.executeQuery("select * from artists")
        while (rs.next()) {
            System.out.println("id = " + rs.getInt("id"))
            System.out.println("artist = " + rs.getString("artist"))
            System.out.println("info = " + rs.getString("info"))
            System.out.println("source = " + rs.getString("source"))
        }
    }

    fun testDB() {
        var connection: Connection? = null
        try {
            connection = createConnection()
            val statement: Statement = createStatement(connection)
            showArtists(statement)
        } catch (e: SQLException) {
            System.err.println(e.getMessage())
        } finally {
            try {
                if (connection != null) connection.close()
            } catch (e: SQLException) {
                System.err.println(e)
            }
        }
    }

    fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
        val dataBase = dbHelper.writableDatabase
        val values = createValuesOfArtist(artist, info)
        dataBase.insert("artists", null, values)
    }

    private fun createValuesOfArtist(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put("artist", artist)
        values.put("info", info)
        values.put("source", 1)
        return values
    }

/** -- Fin -- **/
    fun getInfo(dbHelper: DataBase, artist: String): String? {
        val cursor = getCursor(dbHelper, artist)
        val itemsOfCursor = map(cursor)
        return itemsOfCursor.getOrNull(0) ?: null
    }

    private fun getCursor(dbHelper: DataBase, artist: String): Cursor {
        val dataBase = dbHelper.readableDatabase
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

    private fun map(cursor: Cursor): List<String> {
        val itemsOfCursor: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            itemsOfCursor.add(info)
        }
        cursor.close()
        return itemsOfCursor
    }

}