package com.example.dough;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
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
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putStringArrayList("links",episodes.get(position).getUrls());
                Intent intent = new Intent(view.getContext(), LinkListActivity.class);
                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });
        holder.description.setText(episodes.get(position).getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isDownloadManagerAvailable(context)) {
                            String url = episodes.get(position).getVidURL();
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setDescription("در حال دانلود...");
                            request.setTitle(episodes.get(position).getName());
// in order for this if to run, you must use the android 3.2 to compile your app
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            File dir = new File(Environment.DIRECTORY_DOWNLOADS , "Dough");
                            if (!dir.mkdirs())
                                dir.mkdirs();
                            request.setDestinationInExternalPublicDir(  dir.getAbsolutePath(), episodes.get(position).getName() + ".mkv");

// get download service and enqueue file
                            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }
                    }
                }).start();
                Log.e("as" , "x");
            }
        });


    }


    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView description;
        ImageButton imageButton, play;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
            play = itemView.findViewById(R.id.playy);
            description =  itemView.findViewById(R.id.textView4);
            imageButton = itemView.findViewById(R.id.dlSeriesButton);
        }


    }
    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
}
