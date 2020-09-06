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
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    static final Integer READ_EXST = 0x4;
    private RecyclerView movierecview, seriesrecview;
    private RecyclerView downloadedFilmRecylclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    static ArrayList<Movie> movies = new ArrayList<>();
    static ArrayList<Series> series = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // permissionStuff();
        setContentView(R.layout.activity_main);

        movierecview = findViewById(R.id.movierecview);
        seriesrecview = findViewById(R.id.seriesrecview);
        downloadedFilmRecylclerView = findViewById(R.id.downloadedFilms);
        downloadJSON("https://raw.githubusercontent.com/rimthekid/Dough-mas/master/output2.json", false);
        downloadJSON("https://raw.githubusercontent.com/rimthekid/Dough-mas/master/series.json", true);



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
                        loadIntoListView(l20.toString().trim(), true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    writeFileOnInternalStorage(MainActivity.this, "seriesJson.json", s);
                } else {
                    try {
                        loadIntoListView(l20.toString().trim(), false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    writeFileOnInternalStorage(MainActivity.this, "moviesJson.json", s);
                }
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    int i = 0;
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                        i++;
                        if (i < 11) {
                            l20.append(json + "\n");
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
        if (!isSeries) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<Movie>>() {
            }.getType();
            Collection<Movie> moviezz = gson.fromJson(json, collectionType);
            for (Movie movie : moviezz) {
                movie.setName(movie.getName().replaceAll("\\.", " "));
            }
            movies.addAll(moviezz);
            System.out.println(movies.get(2).getImgURL());
            System.out.println("kir");

            //Toast.makeText(getApplicationContext(),"kk", Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<Series>>() {
            }.getType();
            Collection<Series> seriezz = gson.fromJson(json, collectionType);
            for (Series seriezz1 : seriezz) {
                seriezz1.setName(seriezz1.getName().replaceAll("\\.", " "));
            }
            series.addAll(seriezz);
            System.out.println(series.get(2).getImgURL());
            System.out.println("kos");
        }


        /*File file = new File(Environment.DIRECTORY_DOWNLOADS + "/folderName");
        if (!file.mkdirs()) {
            file.mkdirs();
        }*/

        //System.out.println(movies.get(0).getVidurl());
        File fileTMP = new File(Environment.DIRECTORY_DOWNLOADS + "Dough");
        File directory = Environment.getExternalStoragePublicDirectory(fileTMP.getAbsolutePath());


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
            if (movie != null) {
                movie.setMovieFile(file);
                downloadedMovies.add(movie);
            }
        }
        refreshList(movies , series);
        DownloadedMoviesAdapter downloadedMoviesAdapter = new DownloadedMoviesAdapter(downloadedMovies, this);
        downloadedFilmRecylclerView.setAdapter(downloadedMoviesAdapter);
        downloadedFilmRecylclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSwipeRefreshLayout.setRefreshing(false);
        refreshList(movies , series);
        search();
    }

    private void refreshList(ArrayList<Movie> movieArrayList , ArrayList<Series> seriesArrayList) {
        MovieRecViewAdapter adapter = new MovieRecViewAdapter(this);
        adapter.setMovie(movieArrayList);
        movierecview.setAdapter(adapter);
        movierecview.setLayoutManager(new GridLayoutManager(this, 3));
        SeriesRecViewAdapter adapterr = new SeriesRecViewAdapter(this);
        adapterr.setSeries(seriesArrayList);
        seriesrecview.setAdapter(adapterr);
        seriesrecview.setLayoutManager(new GridLayoutManager(this, 3));
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
        refreshList(movies,series);
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

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody) {
        File dir = new File(mcoContext.getFilesDir(), "json");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search() {
        final ImageButton search = findViewById(R.id.imageButton4);
        final EditText editText = findViewById(R.id.editTextTextPersonName);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                Log.e("SA", text);
                File dir = new File(MainActivity.this.getFilesDir(), "json");
                File seriesJson = new File(dir, "seriesJson.json");
                File moviesJson = new File(dir, "moviesJson.json");
                try {
                    FileReader movieReader = new FileReader(moviesJson);
                    FileReader seriesReader = new FileReader(seriesJson);
                    Gson gson = new Gson();
                    HashSet<Movie> movies = gson.fromJson(movieReader, new TypeToken<HashSet<Movie>>() {
                    }.getType());
                    HashSet<Series> series = gson.fromJson(seriesReader, new TypeToken<HashSet<Series>>() {
                    }.getType());
                    ArrayMap<Movie, Integer> searchMovieArray = new ArrayMap<>();
                    ArrayMap<Series, Integer> searchSeriesArray = new ArrayMap<>();
                    for (Movie movie : movies) {
                        searchMovieArray.put(movie, checkName(movie.getName(), text));
                    }
                    for (Series series1 : series) {
                        searchSeriesArray.put(series1, checkName(series1.getName(), text));
                    }
                    ArrayList<Movie> movieArrayListSearch = new ArrayList<>();
                    ArrayList<Series> seriesArrayListSearch = new ArrayList<>();

                    int max = 0;
                    sortMovieArray(searchMovieArray, movieArrayListSearch, max);
                    sort(searchSeriesArray, seriesArrayListSearch, max);
                    refreshList(movieArrayListSearch , seriesArrayListSearch);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sortMovieArray(ArrayMap<Movie, Integer> searchMovieArray, ArrayList<Movie> movieArrayListSearch, int max) {
        for (int i = 0; i < searchMovieArray.keySet().size(); i++) {
            max = -1;
            if (i != 0)
                searchMovieArray.remove(movieArrayListSearch.get(i - 1));
            if (i == 4)
                return;
            movieArrayListSearch.add(null);

            for (int j = 0; j < searchMovieArray.keySet().size(); j++) {
                if (max < searchMovieArray.get(searchMovieArray.keyAt(j))) {
                    movieArrayListSearch.remove(i);
                    (searchMovieArray.keyAt(j)).setName((searchMovieArray.keyAt(j)).getName().replaceAll("\\.", " "));
                    movieArrayListSearch.add((searchMovieArray.keyAt(j)));
                    max = searchMovieArray.get(searchMovieArray.keyAt(j));
                }
            }
        }
    }

    private void sort(ArrayMap<Series, Integer> searchSeriesArray, ArrayList<Series> seriesArrayListSearch, int max) {
        for (int i = 0; i < searchSeriesArray.keySet().size(); i++) {
            max = 0;
            if (i != 0)
                searchSeriesArray.remove(seriesArrayListSearch.get(i - 1));
            if (i == 4)
                return;
            seriesArrayListSearch.add(null);

            for (int j = i; j < searchSeriesArray.keySet().size(); j++) {
                if (max < searchSeriesArray.get(searchSeriesArray.keyAt(j))) {
                    seriesArrayListSearch.remove(i);
                    seriesArrayListSearch.add((searchSeriesArray.keyAt(j)));
                    searchSeriesArray.keyAt(j).setName(searchSeriesArray.keyAt(j).getName().replaceAll("\\.", " "));
                    max = searchSeriesArray.get(searchSeriesArray.keyAt(j));
                }
            }
        }
    }

    public int checkName(String name1, String name2) {
        char[] name1CharArray = name1.replaceAll("\\.", " ").toCharArray();
        char[] name2CharArray = name2.toCharArray();
        int i = 0;
        Log.e(name1, name2);

        for (int c1 = 0 ; c1 < name2CharArray.length; c1++) {
            for (int c = 0; c < name1CharArray.length; c++) {
                if (name1CharArray[c] == name2CharArray[c1])  {
                    i++;
                    Log.i("SA", String.valueOf(c) + " " +String.valueOf(c1));
                    name1CharArray[c] = '~';
                    c = 100000;
                }
            }
        }
        Log.d("", String.valueOf(i));
        return i;
    }

    @Override
    public void onBackPressed() {
        refreshList(movies , series);
    }
}