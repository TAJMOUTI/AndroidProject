package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Main_Page extends AppCompatActivity {

    private FirebaseUser user;
    private String username;
    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();
        textViewName = findViewById(R.id.textViewName);

        textViewName.setText("Hi " + username);

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