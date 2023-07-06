package com.example.notesharingminiprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.notesharingminiprojectapp.databinding.ActivityLoginSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginSignup extends AppCompatActivity {

    ActivityLoginSignupBinding binding;
    DatabaseReference databaseReference;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Welcome to Notss..");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_blue)));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        binding.txtDontHaveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this,SignupActivity.class);
            startActivity(intent);
            finish();
        });

        binding.loginButton.setOnClickListener(view -> {
            email = binding.emailText.getText().toString().trim();
            password = binding.passwordText.getText().toString().trim();
            
            if(email.length() > 0 && password.length() > 0) {
                login();                
            } else {
                Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginSignup.this,SubjectActivity.class));
            finish();
        }
    }

    private void login() {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(LoginSignup.this,SubjectActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginSignup.this, "Invalid Credentials: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}