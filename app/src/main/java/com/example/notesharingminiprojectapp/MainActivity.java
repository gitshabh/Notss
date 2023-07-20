package com.example.notesharingminiprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.notesharingminiprojectapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseReference databaseReference;
    String subjectCode,subjectName,senderName;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        subjectCode = i.getStringExtra("code");
        subjectName = i.getStringExtra("subjectName");

        getSupportActionBar().setTitle(subjectName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_blue)));

        messageAdapter = new MessageAdapter(MainActivity.this,subjectCode);

        binding.recycler.setAdapter(messageAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        senderName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        databaseReference = FirebaseDatabase.getInstance().getReference("chats").child(subjectCode);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageAdapter.add(messageModel);
                }
                binding.recycler.scrollToPosition(messageAdapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.uploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent,116);
            binding.recycler.scrollToPosition(messageAdapter.getItemCount()-1);
            binding.uploadImage.setEnabled(false);
            binding.progressBar.setProgress(0);
        });

        binding.sendMessage.setOnClickListener(view -> {
            String messageText = binding.chatMessage.getText().toString();
            if(messageText.trim().length() > 0) {
                binding.chatMessage.setText("");
                sendMessage(messageText);
            }
            binding.recycler.scrollToPosition(messageAdapter.getItemCount()-1);
        });

    }

    private void sendMessage(String message) {
        String messageId = databaseReference.push().getKey();

        MessageModel messageModel = new MessageModel(messageId,FirebaseAuth.getInstance().getUid(),message,"text",senderName,"");

        messageAdapter.add(messageModel);

        databaseReference
                .child(messageId)
                .setValue(messageModel);
    }

    @Override
    protected void onActivityResult(int requestsubjectCode, int resultsubjectCode, @Nullable Intent data) {
        super.onActivityResult(requestsubjectCode, resultsubjectCode, data);

        if(requestsubjectCode == 116 && resultsubjectCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String path = fileUri.getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("files").child(fileName);

            UploadTask uploadTask = storageReference.putFile(fileUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String messageId = databaseReference.push().getKey();

                        MessageModel messageModel = new MessageModel(messageId,FirebaseAuth.getInstance().getUid(),downloadUri.toString(),"file",senderName,fileName);

                        messageAdapter.add(messageModel);

                        databaseReference
                                .child(messageId)
                                .setValue(messageModel);

                        binding.uploadImage.setEnabled(true);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    binding.uploadImage.setEnabled(true);
                    Toast.makeText(MainActivity.this, "File Upload Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    binding.progressBar.setProgress((int)progress);
                }
            });
        } else {
            Toast.makeText(this, "File Not Selected", Toast.LENGTH_SHORT).show();
        }

    }
}