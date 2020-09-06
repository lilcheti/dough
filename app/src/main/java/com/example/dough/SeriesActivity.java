package com.example.dough;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SeriesActivity extends AppCompatActivity {

    String imgUrl;
    String seriesName;
    Spinner dropdown;
    RecyclerView seriesRecView;
    String url;
    Series series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        setContentView(R.layout.activity_series);
        imgUrl = getIntent().getStringExtra("imgurl");
        url = getIntent().getStringExtra("url");
        seriesName = getIntent().getStringExtra("seriesName");
        Type type = new TypeToken<Series>() {
        }.getType();

        series = gson.fromJson(getIntent().getStringExtra("seriesObject"), type);
        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this)
                .asBitmap()
                .load(imgUrl)
                .into(imageView);
        final TextView textView = findViewById(R.id.textView3);
        textView.setText(seriesName);
        dropdown = findViewById(R.id.spinner1);
        setSpinner();
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                seriesRecView = findViewById(R.id.seriesRecView);
                SeriesAdapter adapter = new SeriesAdapter(series.getSeasons().get(i ).getEpisodes(), SeriesActivity.this);
                seriesRecView.setAdapter(adapter);
                seriesRecView.setLayoutManager(new GridLayoutManager(SeriesActivity.this, 1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    private void setSpinner() {
        // final ArrayList<String> items = new ArrayList<>();
        class spinna extends AsyncTask<Void, Void, ArrayList<String>> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                ArrayList<String> items = new ArrayList<>();
                int i = 1;
                for (Season season : series.getSeasons()) {
                    items.add(String.valueOf(i));
                    i++;
                }

                return items;

            }

            @Override
            protected void onPostExecute(ArrayList<String> items) {
                super.onPostExecute(items);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeriesActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        }
        spinna kkkk = new spinna();
        kkkk.execute();


    }

    public boolean doesEpisodeExist(ArrayList<Episode> episodes, String episodeName) {
        for (Episode episode : episodes) {
            if (episode.getName().equals(episodeName)) {
                return true;
            }
        }
        return false;
    }

    public Episode findEpisodeByName(ArrayList<Episode> episodes, String name) {
        for (Episode episode : episodes) {
            if (episode.getName().equals(name))
                return episode;
        }
        return null;
    }


}