package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class PictureActivityDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    ListView menuView;
    ArrayList<String> ingredients;
    QuantityAdapter adapter;
    Button validateButton;
    public ArrayList<IngredientModel> editModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);
        recyclerView = findViewById(R.id.view);
        validateButton = (Button) findViewById(R.id.validate);

        ingredients = getIntent().getStringArrayListExtra("INGREDIENTS_LIST");
        editModelArrayList = populateList(ingredients);
        System.out.println("getting ingredients in on create");
        for(int i = 0; i < ingredients.size(); i++) {
            System.out.println(ingredients.get(i));
        }
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuantityAdapter(this, editModelArrayList);
        recyclerView.setAdapter(adapter);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("data", "titles -> "+ Arrays.toString(editModelArrayList.toArray()));
                Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                startActivity(intent);
            }
        });

    }
    // Recupere les ingredients selectionnés pour specifier les quantités en grammes
    private ArrayList<IngredientModel> populateList(ArrayList<String> ingredients){
        ArrayList<IngredientModel> list = new ArrayList<>();
        //Prends tous les ingrédients selectionnés et les place dans le modele Ingredient Model
        for(String item : ingredients){
            IngredientModel editModel = new IngredientModel();
            editModel.setName((String.valueOf(item)));
            editModel.setQuantity("0");
            list.add(editModel);
        }
        return list;
    }

}
