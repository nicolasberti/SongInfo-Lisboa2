package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
    setTextMoreDetails();
    createFMAPI();
    open();
    updateMoreDetails();
  }

  private void setTextMoreDetails(){
    textMoreDetails = findViewById(R.id.textMoreDetails);
  }

  private void updateMoreDetails(){
    Intent intent = getIntent();
    String artistName = intent.getStringExtra(ARTIST_NAME_EXTRA);
    initThreadInfo(artistName);
  }

  private void initThreadInfo(String artistName) {
    Thread hilo = createThreadInfo(artistName);
    startThread(hilo);
  }

  private Thread createThreadInfo(String artistName){
    return new Thread(() -> {
      String info = getInfoDatabase(artistName);
      if (info == null) {
        JsonObject artist = getArtistAPI(artistName);
        JsonElement bio = getBioFromArtist(artist);
        String url = getURLFromArtist(artist);
        info = getInfoFromArtistAPI(bio, artistName);
        if (bio != null)
          saveInfo(artistName, info);
        setListenerURL(url);
      }
      setTextInfo(info);
    });
  }

  private void saveInfo(String artistName, String info){
    DataBase.saveArtist(dataBase, artistName, info);
  }

  private void startThread(Thread hilo){
    hilo.start();
  }

  private JsonElement getBioFromArtist(JsonObject artist){
    JsonObject bio = artist.get("bio").getAsJsonObject();
    return bio.get("content");
  }

  private String getURLFromArtist(JsonObject artist){
    JsonElement url = artist.get("url");
    return url.getAsString();
  }


  private void setListenerURL(String url){
    findViewById(R.id.openUrlButton).setOnClickListener(v -> {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(url));
      startActivity(intent);
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
    return textoArtistaEnNegrita.replaceAll("(?i)" + artist, artist.toUpperCase());
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