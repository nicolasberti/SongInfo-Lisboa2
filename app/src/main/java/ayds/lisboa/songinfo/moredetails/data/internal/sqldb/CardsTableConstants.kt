package ayds.lisboa.songinfo.moredetails.data.internal.sqldb

const val ARTIST_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_COLUMN = "source_logo"
const val URL_COLUMN = "url"
const val DATABASE_VERSION = 1
const val DATABASE_NAME = "dictionary.db"

const val createTableArtists: String =
    "create table $ARTIST_TABLE ("+
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "$ARTIST_COLUMN string, "+
            "$INFO_COLUMN string, "+
            "$URL_COLUMN string, "+
            "$SOURCE_COLUMN string," +
            "$SOURCE_LOGO_COLUMN string)"