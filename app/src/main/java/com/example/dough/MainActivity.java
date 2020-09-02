package com.example.dough;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    static final Integer READ_EXST = 0x4;
    private RecyclerView movierecview;
    private RecyclerView downloadedFilmRecylclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // permissionStuff();
        setContentView(R.layout.activity_main);


        movierecview = findViewById(R.id.movierecview);
        downloadedFilmRecylclerView = findViewById(R.id.downloadedFilms);
        downloadJSON("https://raw.githubusercontent.com/rimthekid/Dough-mas/master/output2.json", false);




    }



    private void downloadJSON(final String urlWebService, final boolean isSeries) {
        final StringBuilder l20 = new StringBuilder();
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
                mSwipeRefreshLayout.setOnRefreshListener(MainActivity.this);
                mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                        android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

                /**
                 * Showing Swipe Refresh animation on activity create
                 * As animation won't start on onCreate, post runnable is used
                 */
                mSwipeRefreshLayout.post(new Runnable() {

                    @Override
                    public void run() {

                        mSwipeRefreshLayout.setRefreshing(true);

                        // Fetching data from server
                    }
                });
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                    if (isSeries) {
                        try {
                            loadIntoListView(l20.toString().trim(),true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        writeFileOnInternalStorage(MainActivity.this,"seriesJson.json",s);
                    }else {
                        try {
                            loadIntoListView(l20.toString().trim(),false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        writeFileOnInternalStorage(MainActivity.this,"moviesJson.json",s);
                      }
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    int i=0;
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                        i++;
                        if (i<11){
                            l20.append(json+"\n");
                        }
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


    private void loadIntoListView(String json, boolean isSeries) throws JSONException {
            if (!isSeries){
                Gson gson = new Gson();
                Type collectionType = new TypeToken<Collection<Movie>>(){}.getType();
                Collection<Movie> moviezz = gson.fromJson(json, collectionType);
                movies.addAll(moviezz);
                System.out.println(movies.get(2).getImgURL());
                System.out.println("kir");
                refreshList();
                //Toast.makeText(getApplicationContext(),"kk", Toast.LENGTH_SHORT).show();
            }else
            {
                Gson gson = new Gson();
                gson.fromJson(json, Series.class);
            }


        /*File file = new File(Environment.DIRECTORY_DOWNLOADS + "/folderName");
        if (!file.mkdirs()) {
            file.mkdirs();
        }*/

        //System.out.println(movies.get(0).getVidurl());
       /* File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        Log.v("Files", directory.exists() + "");
        Log.v("Files", directory.isDirectory() + "");
        Log.v("Files", directory.listFiles() + "");
        File[] files = directory.listFiles();
        ArrayList<Movie> downloadedMovies = new ArrayList<>();
        for (File file : files) {
            System.out.println(file.getName());
            String[] name = file.getName().split("\\.");
            String movieName = filmName(name);
            Movie movie = findMovieByName(movieName, movies);
            movie.setMovieFile(file);
            downloadedMovies.add(movie);
        }
        refreshList();
        DownloadedMoviesAdapter downloadedMoviesAdapter = new DownloadedMoviesAdapter(downloadedMovies, this);
        downloadedFilmRecylclerView.setAdapter(downloadedMoviesAdapter);
        downloadedFilmRecylclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        */mSwipeRefreshLayout.setRefreshing(false);
    }

    private void refreshList() {
        MovieRecViewAdapter adapter = new MovieRecViewAdapter(this);
        adapter.setMovie(movies);
        movierecview.setAdapter(adapter);
        movierecview.setLayoutManager(new GridLayoutManager(this, 3));
        mSwipeRefreshLayout.setRefreshing(false);

    }

    public String filmName(String[] name) {
        String movieName = "";
        for (int i = 0; i < name.length; i++) {
            if (name[i].equals("mkv"))
                return movieName;
            else {
                if (name[i + 1].equals("mkv"))
                    movieName += name[i];
                else movieName += name[i] + " ";
            }
        }
        return null;
    }

    public Movie findMovieByName(String name, ArrayList<Movie> Movies) {
        for (Movie movie : Movies) {
            if (movie.getName().equals(name))
                return movie;
        }
        return null;
    }


    @Override
    public void onRefresh() {
        refreshList();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Log.e("TAg", "permission dary");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent, 11);

            Log.e("TAg", "permission dary");
        } else {
            Log.e("TAg", "permission nadary");
        }
    }


    private void askForPermission(String permission, Integer requestCode) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "json");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}