package com.example.dough;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DownloadedMoviesAdapter extends RecyclerView.Adapter<DownloadedMoviesAdapter.ViewHolder> {

    private ArrayList<movie> dlMovies;
    private LayoutInflater inflater;
    private Context context;

    public DownloadedMoviesAdapter(ArrayList<movie> dlMovies, Context context) {
        this.dlMovies = dlMovies;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.downloaded_films , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(dlMovies.get(position).getImageUrl())
                .into(holder.imageView);
        holder.textView.setText(dlMovies.get(position).getName());
        
    }

    @Override
    public int getItemCount() {
        return dlMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            textView = itemView.findViewById(R.id.textView2);
        }


    }
}
