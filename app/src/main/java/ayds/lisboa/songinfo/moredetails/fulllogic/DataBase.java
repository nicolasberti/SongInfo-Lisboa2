package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    static final String URLConnection = "jdbc:sqlite:./dictionary.db";
    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URLConnection);
    }

    private static Statement createStatement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    private static void showArtists(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("select * from artists");
        while(rs.next())
        {
            System.out.println("id = " + rs.getInt("id"));
            System.out.println("artist = " + rs.getString("artist"));
            System.out.println("info = " + rs.getString("info"));
            System.out.println("source = " + rs.getString("source"));
        }
    }

    public static void testDB()
    {
        Connection connection = null;
        try
        {
            connection = createConnection();
            Statement statement = createStatement(connection);
            showArtists(statement);
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }

  public static void saveArtist(DataBase dbHelper, String artist, String info)
  {
    SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
    ContentValues values = createValuesOfArtist(artist, info);
    dataBase.insert("artists", null, values);
  }

  private static ContentValues createValuesOfArtist(String artist, String info){
    ContentValues values = new ContentValues();
      values.put("artist", artist);
      values.put("info", info);
      values.put("source", 1);
    return values;
  }


  private static Cursor getCursor(DataBase dbHelper, String artist){
    SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
    String[] columnsToUse = { "id", "artist", "info" };
    String columnFilter = "artist  = ?";
    String[] columnFilterArgs = { artist };
    String sortOrderDesc = "artist DESC";
    Cursor cursor = dataBase.query("artists", columnsToUse, columnFilter, columnFilterArgs,null,null, sortOrderDesc);
    return cursor;
  }

  private static List<String> getItemsFromCursor(Cursor cursor){
    List<String> itemsOfCursor = new ArrayList<String>();
    while(cursor.moveToNext()) {
      String info = cursor.getString(cursor.getColumnIndexOrThrow("info"));
      itemsOfCursor.add(info);
    }
    cursor.close();
    return itemsOfCursor;
  }

  public static String getInfo(DataBase dbHelper, String artist)
  {
    Cursor cursor = getCursor(dbHelper, artist);
    List<String> itemsOfCursor = getItemsFromCursor(cursor);
    return itemsOfCursor.isEmpty()? null : itemsOfCursor.get(0);
  }

  public DataBase(Context context) {
    super(context, "dictionary.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase dataBase) {
    String query = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)";
    dataBase.execSQL(query);
    Log.i("DB", "DataBase created");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}
