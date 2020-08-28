package com.example.dough;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SeriesActivity extends AppCompatActivity {

    String imgUrl;
    String seriesName;
    Spinner dropdown;
    RecyclerView seriesRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        imgUrl = getIntent().getStringExtra("imgurl");
        seriesName = getIntent().getStringExtra("seriesName");
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
                final ArrayList<Episode> episodes = new ArrayList<Episode>();
                final ArrayList<String> episodesDescription = new ArrayList<>();
               seriesName = seriesName.replaceAll(":", "");
                class Load extends AsyncTask<Void, Void, Boolean> {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        String url;

                        if (i < 9) {
                            url = "http://dl2.persian2movie.com/mahdip/Series/" + seriesName.replaceAll(" ", "\\.") + "/S0" + (i+1);
                        } else {
                            url = "http://dl2.persian2movie.com/mahdip/Series/" + seriesName.replaceAll(" ", "\\.") + "/S" + String.valueOf(i+1);
                        }
                        Document document = null;

                        try {
                            document = Jsoup.connect(url).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for (Element file : document.select("a")) {
                            Collections.addAll(episodesDescription, file.attr("href").split("/"));
                        }
                        episodesDescription.remove(0);
                        for (int j = 1 ; j <= episodesDescription.size() ; j++ ) {
                            System.out.println(url+"/" + episodesDescription.get(j-1).replaceAll(" " , "\\."));
                            episodes.add(new Episode("S" + (i+1) + "E" + j , imgUrl , url+"/" + episodesDescription.get(j-1).replaceAll(" " , "\\.")) );
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Boolean s) {
                        super.onPostExecute(s);
                        seriesRecView = findViewById(R.id.seriesRecView);
                        SeriesAdapter adapter = new SeriesAdapter(episodes , SeriesActivity.this);
                        seriesRecView.setAdapter(adapter);
                        seriesRecView.setLayoutManager(new GridLayoutManager(SeriesActivity.this, 1));

                    }
                }
                Load kk = new Load();
                kk.execute();





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
                ArrayList<String> sessions = new ArrayList<>();
                Document document = null;
                seriesName = seriesName.replaceAll(":", "");
                try {
                    document = Jsoup.connect("http://dl2.persian2movie.com/mahdip/Series/" + seriesName.replaceAll(" ", "\\.") + "/").get();
                    for (Element file : document.select("a")) {
                        Collections.addAll(sessions, file.text().split("/"));
                    }
                    sessions.remove(0);
                    int i = 1;
                    for (String session : sessions) {
                        items.add(String.valueOf(i));
                        System.out.println(session + i);
                        i++;
                    }
                    return items;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

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

}