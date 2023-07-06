package com.example.notesharingminiprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.notesharingminiprojectapp.databinding.ActivityLoginSignupBinding;
import com.example.notesharingminiprojectapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseReference databaseReference;

    String name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Welcome to Notss..");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_blue)));

        binding.txtAlreadyHaveAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginSignup.class));
            finish();
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        binding.btnSignUp.setOnClickListener(view -> {
            name = binding.txtUsername.getText().toString().trim();
            email = binding.txtEmail.getText().toString().trim();
            password = binding.txtPassword.getText().toString().trim();

            if(email.length() > 0 && password.length() > 0) {
                signUp();
            } else {
                Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void signUp() {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email.trim(),password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseUser.updateProfile(userProfileChangeRequest);
                            UserModel userModel = new UserModel(name,email,password);
                            databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                            Intent intent = new Intent(SignupActivity.this,SubjectActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Error occurred while creating your account: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}