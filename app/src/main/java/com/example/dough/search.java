package com.example.dough;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class search extends BaseActivity {
    private RecyclerView movierecview, seriesrecview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final ImageButton search = findViewById(R.id.imageButton4);
        final EditText editText = findViewById(R.id.editTextTextPersonName);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String text = editText.getText().toString();
                    if (text != null){
                        Log.e("SA", text);
                        File dir = new File(search.this.getFilesDir(), "json");
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
                    File dir = new File(search.this.getFilesDir(), "json");
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

    @Override
    int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_search;
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
    private void refreshList(ArrayList<Movie> movieArrayList, ArrayList<Series> seriesArrayList) {
       // Collections.shuffle(movieArrayList);
        //Collections.shuffle(seriesArrayList);
        ArrayList<Movie> mm = new ArrayList<>();
        ArrayList<Series> ss = new ArrayList<>();
        if (movieArrayList.size() != 0 && seriesArrayList.size() != 0){
            for (int i = 0 ;i<75;i++){
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
        TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            movierecview.setLayoutManager(new GridLayoutManager(this, 4));
            seriesrecview.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            movierecview.setLayoutManager(new GridLayoutManager(this, 3));
            seriesrecview.setLayoutManager(new GridLayoutManager(this, 3));
        }
        SeriesRecViewAdapter adapterr = new SeriesRecViewAdapter(this);
        adapterr.setSeries(ss);
        seriesrecview.setAdapter(adapterr);


    }
}