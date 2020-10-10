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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LinkListActivity extends AppCompatActivity {
    RecyclerView linklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linklist);
        Bundle b = this.getIntent().getExtras();
        final ArrayList<String> Urls = b.getStringArrayList("links");
        linklist = findViewById(R.id.linkList);
        linklistadapter linklistadapter = new linklistadapter(Urls,this);
        linklist.setAdapter(linklistadapter);
        linklist.setLayoutManager(new GridLayoutManager(this,1));


    }

}
