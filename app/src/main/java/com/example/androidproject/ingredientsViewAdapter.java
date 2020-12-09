package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ingredientsViewAdapter extends RecyclerView.Adapter<ingredientsViewAdapter.myViewHolder> {

    Context ctx;
    ArrayList<String> ingredients;
    ArrayList<Integer> portions;

    public ingredientsViewAdapter(Context ctx, ArrayList<String> ingredients, ArrayList<Integer> portions) {
        this.ctx = ctx;
        this.ingredients = ingredients;
        this.portions = portions;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.ingredients_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.oneIngredient.setText(ingredients.get(position));
        holder.portion.setText(portions.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView oneIngredient;
        TextView portion;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            oneIngredient = itemView.findViewById(R.id.oneIngredient);
            portion = itemView.findViewById(R.id.portion);
        }
    }
}
