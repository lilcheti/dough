package com.example.dough;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    ArrayList<Episode> episodes;
    private LayoutInflater inflater;
    private Context context;

    public SeriesAdapter(ArrayList<Episode> episodes,  Context context) {
        this.episodes = episodes;
        this.inflater = LayoutInflater.from(context) ;
        this.context = context;
    }

    @NonNull
    @Override
    public SeriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeriesAdapter.ViewHolder(inflater.inflate(R.layout.series_row , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesAdapter.ViewHolder holder, final int position) {


        Glide.with(context)
                .asBitmap()
                .load(episodes.get(position).getImgURL())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, episodes.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Playmovie.class);
                intent.putExtra("vidurl", episodes.get(position).getVidURL());
                intent.putExtra("inLocal", "download");
                view.getContext().startActivity(intent);
            }
        });
        holder.description.setText(episodes.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
            description =  itemView.findViewById(R.id.textView4);
        }
    }
}
