package com.example.dough;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SeriesRecViewAdapter extends RecyclerView.Adapter<SeriesRecViewAdapter.ViewHolder> {

    private ArrayList<Series> Series = new ArrayList<>();
    private Context context;

    public SeriesRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtName.setText(Series.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SeriesActivity.class);
                    intent.putExtra("imgurl", Series.get(position).getImgURL());
                    intent.putExtra("seriesName", Series.get(position).getName());
                    view.getContext().startActivity(intent);
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(Series.get(position).getImgURL())
                .into(holder.image);
    }



    @Override
    public int getItemCount() {
        return Series.size();
    }

    public void setSeries(ArrayList<Series> Series) {
        this.Series = Series;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;

        private CardView parent;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            parent = itemView.findViewById(R.id.parent);
            image = itemView.findViewById(R.id.image);

        }
    }


}
