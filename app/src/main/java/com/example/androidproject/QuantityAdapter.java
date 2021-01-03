package com.example.androidproject;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder> {
    private LayoutInflater inflater;
    public static ArrayList<IngredientModel> editModelArrayList;

    public QuantityAdapter(Context context, ArrayList<IngredientModel> editModelArrayList){
        this.editModelArrayList = editModelArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.quantity_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(editModelArrayList.get(position).getName());
        holder.quantity.setText(editModelArrayList.get(position).getQuantity());
        holder.card.setBackgroundColor(Color.parseColor("#00F50057"));
    }

    public HashMap<String, String> getQuantities(@NonNull ViewHolder holder) {
        String name, quantity = "";
        HashMap<String, String> list_quantities = new HashMap<String, String>();
        for(int i = 0; i< getItemCount(); i++){
            quantity = holder.quantity.getText().toString();
            System.out.println("Quantity = " + quantity);
            name = holder.name.getText().toString();
            list_quantities.put(name, quantity);
        }

        return list_quantities;
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        EditText quantity;
        View mView;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantityfoodtext);
            card=itemView.findViewById(R.id.quantity_items_card);
            mView = itemView;

            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editModelArrayList.get(getAdapterPosition()).setQuantity(quantity.getText().toString());
                    System.out.println("ON TEXT CHANGED");
                    System.out.println(editModelArrayList.get(getAdapterPosition()).getName());
                    System.out.println(editModelArrayList.get(getAdapterPosition()).getQuantity());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
    }
}