package com.example.dough;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieRecViewAdapter extends RecyclerView.Adapter<MovieRecViewAdapter.ViewHolder> {

    private ArrayList<movie> movie = new ArrayList<>();
    private Context context;

    public MovieRecViewAdapter(Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(movie.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.activity_popup, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                //Make Inactive Items Outside Of PopupWindow
                boolean focusable = true;

                //Create a window with our parameters
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 50, 50);
                TextView test2 = popupView.findViewById(R.id.textView);
                test2.setText(movie.get(position).getName());
                ImageView imageView = popupView.findViewById(R.id.imageView);
                Glide.with(context)
                        .asBitmap()
                        .load(movie.get(position).getImageUrl())
                        .into(imageView);
                ImageButton imageButton = popupView.findViewById(R.id.imageButton);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, movie.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), Playmovie.class);
                        intent.putExtra("vidurl", movie.get(position).getVidurl());
                        view.getContext().startActivity(intent);
                    }
                });
                ImageButton imageButton1 = popupView.findViewById(R.id.imageButton2);
                imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                ImageButton imageButton2 = popupView.findViewById(R.id.imageButton3);
                imageButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + "data");
                                    myDir.mkdirs();

                                   /* String fname = 1 + ".mkv";
                                    File file = new File(myDir, fname);
                                    if (file.exists()) file.delete();
                                    file.createNewFile();*/
                                    URL u = new URL(movie.get(position).getVidurl());
                                    InputStream is = u.openStream();
                                    DataInputStream dis = new DataInputStream(is);

                                    byte[] buffer = new byte[1024];
                                    int length;
                                    FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "1"));
                                    while ((length = dis.read(buffer)) > 0) {
                                        fos.write(buffer, 0, length);
                                        System.out.println("salam");
                                    }

                                } catch (MalformedURLException mue) {
                                    Log.e("SYNC getUpdate", "malformed url error", mue);
                                } catch (IOException ioe) {
                                    Log.e("SYNC getUpdate", "io error", ioe);
                                } catch (SecurityException se) {
                                    Log.e("SYNC getUpdate", "security error", se);
                                }
                            }
                        }).start();

                    }
                });
            }
        });
        Glide.with(context)
                .asBitmap()
                .load(movie.get(position).getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movie.size();
    }

    public void setMovie(ArrayList<com.example.dough.movie> movie) {
        this.movie = movie;
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
