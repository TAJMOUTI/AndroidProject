package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class activity_sign_up_page extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();
        editTextEmailAddress =      findViewById(R.id.editTextEmailAddress);
        editTextPassword =          findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword =   findViewById(R.id.editTextConfirmPassword);
        editTextName =              findViewById(R.id.editTextName);

        TextView txt_sign_in = (TextView) findViewById(R.id.txt_sign_in);
        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_sign_in_page.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignIn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "on click !");
                String email = editTextEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String name = editTextName.getText().toString();

                if(!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !name.isEmpty()) {
                    if(password.equals(confirmPassword)) {
                        Log.d("DEBUG", "in all if condition");
                        createAccount(email, password, name);
                    } else {
                        Toast.makeText(getApplicationContext(), "passwords don't match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createAccount(String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest setName = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    user.updateProfile(setName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(), "Error while updating name.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void updateUI(FirebaseUser account){
    }
}