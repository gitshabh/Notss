package com.example.notesharingminiprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Subjects> list=new ArrayList<>();
    SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        getSupportActionBar().setTitle("Hello, " + displayName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_blue)));

        recyclerView = findViewById(R.id.subjectRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list.add(new Subjects(R.drawable.compiler,"System Software","18CS61"));
        list.add(new Subjects(R.drawable.graphics,"Computer Graphics","18CS62"));
        list.add(new Subjects(R.drawable.webdev,"Web Technologies","18CS63"));
        list.add(new Subjects(R.drawable.mining,"Data Mining","18CS641"));
        list.add(new Subjects(R.drawable.cloud,"Cloud Computing","18cs643"));
        list.add(new Subjects(R.drawable.analytics,"Data Analytics","18ME653"));

        adapter = new SubjectAdapter(this,list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SubjectActivity.this,LoginSignup.class));
            finish();
            return true;
        }
        return false;
    }

}