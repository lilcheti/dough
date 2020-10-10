package com.example.dough;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class linklistadapter extends RecyclerView.Adapter<linklistadapter.ViewHolder> {
    ArrayList<String> Urls ;
    private LayoutInflater inflater;
    private Context context;

    public linklistadapter(ArrayList<String> urls, Context context) {
        Urls = urls;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public linklistadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new linklistadapter.ViewHolder(inflater.inflate(R.layout.linklistitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull linklistadapter.ViewHolder holder, final int position) {

        String[] kk = Urls.get(position).split("/");
        String vid = kk[Array.getLength(kk)-1];
        vid = vid.replaceAll("\\.", " ");
        vid = vid.replaceAll("%20", " ");
        vid = vid.replaceAll("_", " ");
        vid = vid.replaceAll("-", " ");
        holder.name.setText(vid);
        final String finalVid = vid;
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, finalVid, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Playmovie.class);
                intent.putExtra("vidurl",Urls.get(position));
                intent.putExtra("inLocal", "download");
                view.getContext().startActivity(intent);
            }
        });

        holder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, finalVid, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Playmovie.class);
                intent.putExtra("vidurl",Urls.get(position));
                intent.putExtra("inLocal", "download");
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton play,dl;
        TextView name;
        RelativeLayout whole;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.imgplay);
            dl = itemView.findViewById(R.id.imgdl);
            name = itemView.findViewById(R.id.linkName);
            whole = itemView.findViewById(R.id.whole);
        }
    }
}