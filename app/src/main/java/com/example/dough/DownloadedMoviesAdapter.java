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

public class DownloadedMoviesAdapter extends RecyclerView.Adapter<DownloadedMoviesAdapter.ViewHolder> {

    private ArrayList<Movie> dlMovies;
    private LayoutInflater inflater;
    private Context context;

    public DownloadedMoviesAdapter(ArrayList<Movie> dlMovies, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context)
                .asBitmap()
                .load(dlMovies.get(position).getImgURL())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, dlMovies.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Playmovie.class);
                intent.putExtra("movieFilePath" , dlMovies.get(position).getMovieFile().getAbsolutePath());
                intent.putExtra("inLocal", "inLocal");
                view.getContext().startActivity(intent);
            }
        });
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
