package com.example.dough;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LinkListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linklist);
        ListView linkList = findViewById(R.id.linkList);
        Bundle b = this.getIntent().getExtras();
        final ArrayList<String> Urls = b.getStringArrayList("links");
        // Assign adapter to ListView
        linkList.setAdapter(new linklistadapter(Urls,this));
        linkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String kk = Urls.get(i);
                //Toast.makeText(this, kk, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Playmovie.class);
                intent.putExtra("vidurl",Urls.get(i));
                intent.putExtra("inLocal", "download");
                view.getContext().startActivity(intent);
            }
        });



    }

}
