package com.example.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    // initialize variable
    int[] images;

    // create constructor
    public ViewPagerAdapter(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_viewpager, parent, false);

        // return view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        // set image on imageView
        holder.imageView.setBackgroundResource(images[position]);
    }

    @Override
    public int getItemCount() {
        // return array length
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // initialize variable
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
