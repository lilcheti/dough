package com.example.dough;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
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
import java.util.Collections;
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TabLayout tabLayout = new TabLayout(MainActivity.this);
        tabLayout = findViewById(R.id.tabb);
        movierecview = findViewById(R.id.movierecview);
        seriesrecview = findViewById(R.id.seriesrecview);
        downloadedFilmRecylclerView = findViewById(R.id.downloadedFilms);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    movierecview.setVisibility(RecyclerView.VISIBLE);
                    seriesrecview.setVisibility(RecyclerView.GONE);

                }else if (tab.getPosition() == 1){
                    movierecview.setVisibility(RecyclerView.GONE);
                    seriesrecview.setVisibility(RecyclerView.VISIBLE);
                }
                //do stuff here
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        downloadJSON("https://raw.githubusercontent.com/rimthekid/Dough-mast/master/movies.json", false);
        downloadJSON("https://raw.githubusercontent.com/rimthekid/Dough-mast/master/series.json", true);
        //refreshList(movies, series);
        search();
        //dodol("https://raw.githubusercontent.com/rimthekid/Dough-mas/master/moviesKol.json", false);
        //dodol("https://raw.githubusercontent.com/rimthekid/Dough-mas/master/seriesKol.json", true);


    }


    private void downloadJSON(final String urlWebService, final boolean isSeries) {

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
                if (s!=null){
                if (isSeries) {
                    try {
                        loadIntoListView(s.trim(), true);
                        writeFileOnInternalStorage(MainActivity.this, "seriesJson.json", s);
                    } catch (JSONException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        loadIntoListView(s.trim(), false);
                        writeFileOnInternalStorage(MainActivity.this, "moviesJson.json", s);
                    } catch (JSONException | FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Connection problem!");

                    builder.setMessage("please check your connection.")
                            .setCancelable(false)
                            .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            })
                            .setNegativeButton("try again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    recreate();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();}
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


    private void loadIntoListView(String json, boolean isSeries) throws JSONException, FileNotFoundException {
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
           // System.out.println(series.get(2).getImgURL());
            System.out.println("kos");
        }


        /*File file = new File(Environment.DIRECTORY_DOWNLOADS + "/folderName");
        if (!file.mkdirs()) {
            file.mkdirs();
        }*/

        //System.out.println(movies.get(0).getVidurl());
       /* System.out.println(Environment.DIRECTORY_DOWNLOADS);
        File fileTMP = new File(Environment.DIRECTORY_DOWNLOADS , "Dough");
        if (!fileTMP.mkdirs()) {
        fileTMP.mkdir();}
        File directory = Environment.getExternalStoragePublicDirectory(fileTMP.getAbsolutePath());


        Log.v("Files", directory.exists() + "");
        Log.v("Files", directory.isDirectory() + "");
        Log.v("Files", directory.listFiles() + "");

        File[] files = directory.listFiles();
        ArrayList<Movie> downloadedMovies = new ArrayList<>();
        if (files!= null) {
            for (File file : files) {
                System.out.println(file.getName());
                String[] name = file.getName().split("\\.");
                String movieName = filmName(name);
                File dir = new File(MainActivity.this.getFilesDir(), "json");

                File seriesJson = new File(dir, "seriesJson.json");
                File moviesJson = new File(dir, "moviesJson.json");
                FileReader movieReader = new FileReader(moviesJson);
                FileReader seriesReader = new FileReader(seriesJson);
                Gson gson = new Gson();
                ArrayList<Movie> movies = gson.fromJson(movieReader, new TypeToken<ArrayList<Movie>>() {
                }.getType());
                ArrayList<Series> series = gson.fromJson(seriesReader, new TypeToken<ArrayList<Series>>() {
                }.getType());
                Movie movie = findMovieByName(movieName, movies, series);
                if (movie != null) {
                    movie.setMovieFile(file);
                    downloadedMovies.add(movie);
                }
            }
            DownloadedMoviesAdapter downloadedMoviesAdapter = new DownloadedMoviesAdapter(downloadedMovies, this);
            downloadedFilmRecylclerView.setAdapter(downloadedMoviesAdapter);
            downloadedFilmRecylclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            downloadedFilmRecylclerView.setVisibility(View.VISIBLE);*/
        refreshList(movies, series);

        }



    private void refreshList(ArrayList<Movie> movieArrayList, ArrayList<Series> seriesArrayList) {
        Collections.shuffle(movieArrayList);
        Collections.shuffle(seriesArrayList);
        ArrayList<Movie> mm = new ArrayList<>();
        ArrayList<Series> ss = new ArrayList<>();
        if (movieArrayList.size() != 0 && seriesArrayList.size() != 0){
        for (int i = 0 ;i<40;i++){
            try {
                mm.add(movieArrayList.get(i));
                ss.add(seriesArrayList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }}
        MovieRecViewAdapter adapter = new MovieRecViewAdapter(this);
        adapter.setMovie(mm);
        movierecview.setAdapter(adapter);
        movierecview.setLayoutManager(new GridLayoutManager(this, 3));
        SeriesRecViewAdapter adapterr = new SeriesRecViewAdapter(this);
        adapterr.setSeries(ss);
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

    public Movie findMovieByName(String name, ArrayList<Movie> movies ,ArrayList<Series> seriesArrayList) {
        for (Movie movie : movies) {
            System.out.println(name  + "  " + movie.getName());
            if (movie.getName().replaceAll("\\." , " ").equals(name))
                return movie;
        }
        for (Series series : seriesArrayList) {
            for (Season season : series.getSeasons()) {
                for (Episode episode : season.episodes) {
                    System.out.println(name  + "  " + episode.getName());
//                    if (episode.getName().equals(name))
                        return episode;
                }
            }
        }
        return null;
    }


    @Override
    public void onRefresh() {
        refreshList(movies, series);
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
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String text = editText.getText().toString();
                    if (text != null){
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
                            refreshList(movieArrayListSearch, seriesArrayListSearch);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }}
                    return true;
                }
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (text != null){
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
                    refreshList(movieArrayListSearch, seriesArrayListSearch);
                } catch (IOException e) {
                    e.printStackTrace();
                }}

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

        for (int c1 = 0; c1 < name2CharArray.length; c1++) {
            for (int c = 0; c < name1CharArray.length; c++) {
                if (name1CharArray[c] == name2CharArray[c1]) {
                    i++;
                    Log.i("SA", String.valueOf(c) + " " + String.valueOf(c1));
                    name1CharArray[c] = '~';
                    c = 100000;
                }
            }
        }
        Log.d("", String.valueOf(i));
        return i;
    }


    public void onBackPressed() {
        refreshList(movies, series);
    }
    private void dodol(final String urlWebService, final boolean isSeries) {

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
                    writeFileOnInternalStorage(MainActivity.this, "seriesJson.json", s);
                } else {
                    writeFileOnInternalStorage(MainActivity.this, "moviesJson.json", s);
                }
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
                refreshList(movies, series);
                search();

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

}