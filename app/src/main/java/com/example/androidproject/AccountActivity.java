package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AccountActivity extends AppCompatActivity {

    private FirebaseUser user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // declare and initialise variable
        user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();
        final TextView lblUsername = findViewById(R.id.lblUsername);
        final Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        final Button btnChangePassword = findViewById(R.id.btnChangePassword);
        final Button btnLogOut = findViewById(R.id.btnLogOut);
        final Button btnSetUsername= findViewById(R.id.btnSetUsername);
        final Button btnSetPassword= findViewById(R.id.btnSetPassword);
        final LinearLayout layoutNewUsername = findViewById(R.id.layoutNewUsername);
        final LinearLayout layoutNewPassword = findViewById(R.id.layoutNewPassword);
        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);
        final EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);
        final EditText editTextTextConfirmPassword = findViewById(R.id.editTextTextConfirmPassword);

        // add property
        lblUsername.setText(user.getDisplayName());
        layoutNewUsername.setVisibility(View.GONE);
        layoutNewPassword.setVisibility(View.GONE);
        editTextUsername.setText(username);

        // onclick listeners
        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutNewUsername.getVisibility() == View.GONE) {
                    layoutNewUsername.setVisibility(View.VISIBLE);
                    layoutNewPassword.setVisibility(View.GONE);
                } else {
                    layoutNewUsername.setVisibility(View.GONE);
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutNewPassword.getVisibility() == View.GONE) {
                    layoutNewPassword.setVisibility(View.VISIBLE);
                    layoutNewUsername.setVisibility(View.GONE);
                } else {
                    layoutNewPassword.setVisibility(View.GONE);
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Landing_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnSetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextUsername.getText().toString();
                UserProfileChangeRequest setName = new UserProfileChangeRequest.Builder().setDisplayName(newUsername).build();
                user.updateProfile(setName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), Landing_page.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(AccountActivity.this, "Success ! Log in to see the changes", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Error while updating name.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();
                final String newPassword = editTextNewPassword.getText().toString();
                final String confirmPassword = editTextTextConfirmPassword.getText().toString();

                // we try to re-authenticate to check if the password is correct
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            // check if password match
                            if(!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                                if(newPassword.equals(confirmPassword)) {
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Intent intent = new Intent(getApplicationContext(), Landing_page.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                Toast.makeText(AccountActivity.this, "Success ! Log in to see the changes", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error while updating password.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(AccountActivity.this, "Password don't match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "please fill all field", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}