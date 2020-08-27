package com.example.dough;

import androidx.appcompat.app.AppCompatActivity;

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
        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);

//create a list of items for the spinner.
        final ArrayList<String> items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        dropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //adapter.notifyDataSetChanged();
                textView.setText("salam");
                System.out.println(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("sa;a,");
            }

        });

    }

}