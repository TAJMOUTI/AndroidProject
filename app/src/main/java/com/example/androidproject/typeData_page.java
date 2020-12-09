package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class typeData_page extends AppCompatActivity {

    RecyclerView recyclerViewIngredients;
    EditText editTextTextIngredient;
    EditText editTextPortion;
    Button buttonAdd;
    ArrayList<String> ingredients;
    ArrayList<Integer> portions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_data_page);

        recyclerViewIngredients =   findViewById(R.id.recyclerViewIngredients);
        editTextTextIngredient =    findViewById(R.id.editTextTextIngredient);
        editTextPortion =           findViewById(R.id.editTextPortion);
        buttonAdd =                 findViewById(R.id.buttonAdd);
        ingredients  = new ArrayList<String>();
        portions = new ArrayList<Integer>();

        ingredientsViewAdapter adapter = new ingredientsViewAdapter(this, ingredients, portions);
        recyclerViewIngredients.setAdapter(adapter);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = editTextTextIngredient.getText().toString();
                Integer portion = Integer.valueOf(editTextPortion.getText().toString());

                ingredients.add(ingredient);
                portions.add(portion);

                Log.d("Debug : ", String.valueOf(ingredients));
                Log.d("Debug : ", String.valueOf(portions));

                editTextTextIngredient.setText("");
                editTextPortion.setText("");
            }
        });
    }
}