package com.example.dough;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.gun0912.tedpermission.TedPermission.TAG;

public class DataBase {
    private static final YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();

    private static ArrayList<Movie> movies;

    public static ArrayList<Movie> getMovies() {
        return movies;
    }

    public static YaGson getYaGson() {
        return yaGson;
    }

    public static void setMovies(ArrayList<Movie> movies) {
        DataBase.movies = movies;
    }

    public DataBase() throws IOException {

        FileWriter fileWriter = new FileWriter(new File("Auction.json"));
        Type type = new TypeToken<ArrayList<Movie>>() {
        }.getType();
        yaGson.toJson(movies, type, fileWriter);
        fileWriter.close();
       // downloadJSON();

    }
    private void downloadJSON(final String urlWebService, final boolean isSeries , final String seriesUrl , final String seriesName) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    yaGson.toJson(movies , Long.parseLong(s));
                    Log.e(TAG, movies.get(0).getName() );
                } catch (Exception e) {
                    // be tokhmam
                }

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
/*    private static ArrayList<String> urls = new ArrayList<>();
    private static DataBase dataBase = new DataBase();

    public static DataBase getDataBase() {
        return dataBase;
    }

    private DataBase() {
       // urls.add("http://dl2.persian2movie.com/mahdip/Series/");
        urls.add("http://dl1.3rver.org/hex1/Series/");
    }

    public ArrayList<String> getUrls() {
        return urls;
    }
    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        urls.add(json);
                    }
                    return null;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }*/
}
