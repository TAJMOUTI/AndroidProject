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

public class activity_sign_in_page extends AppCompatActivity {

    private final String TAG = "DEBUG";
    private FirebaseAuth mAuth;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button btnSignIn;
    private TextView txt_sign_up;
    private TextView textViewForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        // initialize variables
        mAuth = FirebaseAuth.getInstance();
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextNewPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        // onclick listener on text sign up
        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_sign_up_page.class);
                startActivity(intent);
                finish();
            }
        });

        // onclick on button let's go
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();
                signIn(email, password);
            }
        });

        // onclick text forgot password
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sign In an user from firebase with email and password
     * need mAuth : FirebaseAuth mAuth = FirebaseAuth.getInstance();
     * @param email
     * @param password
     */
    public void signIn(String email, String password) {
        if(!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        // go to the next page
                        Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(activity_sign_in_page.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                }
            });
        } else {
            Toast.makeText(activity_sign_in_page.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {

    }
}