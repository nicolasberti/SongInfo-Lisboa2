package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ayds.lisboa.songinfo.R;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OtherInfoWindow extends AppCompatActivity {

  public final static String ARTIST_NAME_EXTRA = "artistName";
  private DataBase dataBase = null;
  private TextView textMoreDetails;
  private LastFMAPI lastFMAPI;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_other_info);
    textMoreDetails = findViewById(R.id.textMoreDetails);
    createFMAPI();
    open();
    getArtistInfo(getIntent().getStringExtra("artistName"));
  }

  public void getArtistInfo(String artistName) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            String info = getInfoDatabase(artistName);
            if (info == null) {
                JsonObject artist = getArtistAPI(artistName);
                JsonObject bio = artist.get("bio").getAsJsonObject();
                JsonElement extract = bio.get("content");
                JsonElement url = artist.get("url");
                info = getInfoFromArtistAPI(extract, artistName);
                if (extract != null)
                  DataBase.saveArtist(dataBase, artistName, info);
                setListenerURL(url.getAsString());
            }
            setTextInfo(info);
          }
        }).start();
  }

  private void setListenerURL(String url){
    findViewById(R.id.openUrlButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
      }
    });
  }

  private void open() {
    dataBase = new DataBase(this);
  }

  private String textToHtml(String text) {
    StringBuilder builder = new StringBuilder();
    builder.append("<html><div width=400>");
    builder.append("<font face=\"arial\">");
    builder.append(text);
    builder.append("</font></div></html>");
    return builder.toString();
  }
  private String formatOtherInfo(String info, String artist) {
    String textoSinComillas =  info.replace("'", " ");
    String textoConSaltosDeLineaHTML = textoSinComillas.replace("\n", "<br>");
    String textoArtistaEnNegrita = textoConSaltosDeLineaHTML.replaceAll("(?i)" + artist, "<b>" + artist + "</b>");
    String textoArtistaEnMayusculas = textoArtistaEnNegrita.replaceAll("(?i)" + artist, artist.toUpperCase());
    return textoArtistaEnMayusculas;
  }

  private void setTextInfo(String info){
    String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";
    final String finalText = info;
    runOnUiThread( () -> {
      Picasso.get().load(imageUrl).into((ImageView) findViewById(R.id.imageView));
      textMoreDetails.setText(Html.fromHtml( finalText));
    });
  }

    private JsonObject getArtistAPI(String artistName){
      Response<String> callResponse;
      JsonObject artist = null;
      try{
        callResponse = lastFMAPI.getArtistInfo(artistName).execute();
        Gson gson = new Gson();
        JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
        artist = jobj.get("artist").getAsJsonObject();
      } catch (IOException e1) {
        Log.e("TAG", "Error " + e1);
        e1.printStackTrace();
      }
      return artist;
    }

    private String getInfoFromArtistAPI(JsonElement extract, String artistName){
      String info = "No Results";
      if (extract != null) {
        info = extract.getAsString().replace("\\n", "\n");
        String otherInfoFormated = formatOtherInfo(info, artistName);
        info = textToHtml(otherInfoFormated);
      }
      return info;
    }

    private String getInfoDatabase(String artistName){
      String info = DataBase.getInfo(dataBase, artistName);
      if (info != null)
        info = "[*]" + info;
      return info;
    }

    private void createFMAPI(){
      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl("https://ws.audioscrobbler.com/2.0/")
              .addConverterFactory(ScalarsConverterFactory.create())
              .build();
      lastFMAPI = retrofit.create(LastFMAPI.class);
    }



  }