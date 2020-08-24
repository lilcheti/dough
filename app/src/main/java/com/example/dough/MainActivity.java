package com.example.dough;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView movierecview;
    private RecyclerView downloadedFilmRecylclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movierecview = findViewById(R.id.movierecview);
        downloadedFilmRecylclerView = findViewById(R.id.downloadedFilms);
       downloadJSON("https://raw.githubusercontent.com/cppox/Dough/master/movies.json");

    }
    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoListView(s);
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(s);
                }
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

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<movie> movies = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            movies.add(new movie(obj.getString("name"),obj.getString("image"),obj.getString("vidurl")));
            //Toast.makeText(getApplicationContext(),"kk", Toast.LENGTH_SHORT).show();
        }
        MovieRecViewAdapter adapter = new MovieRecViewAdapter(this);
        adapter.setMovie(movies);
        movierecview.setAdapter(adapter);
        movierecview.setLayoutManager(new GridLayoutManager(this, 3));
        DownloadedMoviesAdapter downloadedMoviesAdapter = new DownloadedMoviesAdapter(movies , this );
        downloadedFilmRecylclerView.setAdapter(downloadedMoviesAdapter);
        downloadedFilmRecylclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }



}