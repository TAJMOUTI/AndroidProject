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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class typeData_page extends AppCompatActivity {

    private final static String TAG = "typeData_page";
    private RecyclerView recyclerViewIngredients;
    private EditText editTextTextIngredient;
    private EditText editTextPortion;
    private Button buttonAdd;
    private Button btnSave;
    private ArrayList<String> ingredients;
    private ArrayList<Integer> portions;
    private FirebaseFirestore db;
    private String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_data_page);

        recyclerViewIngredients =   findViewById(R.id.recyclerViewIngredients);
        editTextTextIngredient =    findViewById(R.id.editTextTextIngredient);
        editTextPortion =           findViewById(R.id.editTextPortion);
        buttonAdd =                 findViewById(R.id.buttonAdd);
        btnSave =                   findViewById(R.id.btnSave);
        ingredients  = new ArrayList<String>();
        portions = new ArrayList<Integer>();
        db = FirebaseFirestore.getInstance();
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        editTextTextIngredient.setText("");
        editTextPortion.setText("");

        ingredientsViewAdapter adapter = new ingredientsViewAdapter(this, ingredients, portions);
        recyclerViewIngredients.setAdapter(adapter);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = editTextTextIngredient.getText().toString();
                Log.d(TAG, "onClick: " + (ingredient.isEmpty()));
                Log.d(TAG, "onClick: " + (editTextPortion.getText().toString().isEmpty()));
                if(!ingredient.isEmpty() && !editTextPortion.getText().toString().isEmpty()) {
                    Integer portion = Integer.valueOf(editTextPortion.getText().toString());
                    ingredients.add(ingredient);
                    portions.add(portion);

                    Log.d("Debug : ", String.valueOf(ingredients));
                    Log.d("Debug : ", String.valueOf(portions));

                    editTextTextIngredient.setText("");
                    editTextPortion.setText("");
                } else Toast.makeText(typeData_page.this, "fill all fields", Toast.LENGTH_LONG).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingredients.size() != 0) {
                    Map<String, Object> notebook = new HashMap<>();
                    Log.d(TAG, "onClick: " + ingredients.toString());
                    notebook.put("list", ingredients);
                    notebook.put("date", new Date());

                    db.collection("users").document(USER_ID).collection("notebook").add(notebook)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(typeData_page.this, "Saved !", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(typeData_page.this, "Fail", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                } else {
                    Toast.makeText(typeData_page.this, "Nothing to save", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}