package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_Page extends AppCompatActivity {

    private FirebaseUser user;
    private String username;
    private TextView textViewName;
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //username = user.getDisplayName(); //todo: erreur firebase
        textViewName = findViewById(R.id.textViewName);

        textViewName.setText("Hi " + username);

        Button btn_to_camera = findViewById(R.id.btn_to_camera);
        btn_to_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PictureActivity.class);
                startActivity(intent);
            }
        });

        Button btn_to_type_data = findViewById(R.id.btn_to_type_data);
        btn_to_type_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), typeData_page.class);
                startActivity(intent);
            }
        });
    }
}