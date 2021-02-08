package com.example.imageuploading__retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imageuploading__retrofit.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MYviewHolder> {
    ArrayList<Bitmap> bitmapArray;
    List<File> files=new ArrayList<>();
    Context context;
    Bitmap bitmap;
    Bitmap equalsqw;
    View v;
    ArrayList<Bitmap> uploadedBITS;
    ItemThumbnailListner itemThumbnailListner;
    Bitmap bitmapq;
    int currentPosition = -1;
    public void fileMOthod(List<File> files){
        this.files=files;
        notifyDataSetChanged();
    }

    public RecyclerAdapter(@NonNull ArrayList<Bitmap> uploadedBITS,ArrayList<Bitmap> bitmapArray, Context applicationContext) {
        this.bitmapArray = bitmapArray;
        this.context=applicationContext;
        this.uploadedBITS=uploadedBITS;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MYviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitems,null);
        return new MYviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MYviewHolder holder,final int position) {
     /*   Uri uri=Uri.fromFile(files.get(position));
       try {
            bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
       Glide.with(context)
               .load(bitmapArray.get(position))
               .into(holder.imageView);

     //    holder.imageView.setImageBitmap(bitmapArray.get(position));

        holder.closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      bitmapArray.remove(position);
                      uploadedBITS.remove(position);
                      files.remove(position);
                    notifyDataSetChanged();

            }
        });
   //     Log.d("Files  Adapter:", bitmapArray.get(position).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    bitmapq=bitmapArray.get(position);
                Intent intent = new Intent(v.getContext(),Thumbnail_Image.class);
                intent.putExtra("image_url",bitmapArray.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
          //      holder.th.setImageBitmap(uploadedBITS.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return bitmapArray.size();
    }

    public class MYviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView closes;
        public MYviewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=v.findViewById(R.id.image_recycle_id);
            closes=v.findViewById(R.id.closs);
        }
    }
}
