package com.example.notesharingminiprojectapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
    Context context;
    List<Subjects> list;

    public SubjectAdapter(Context context, List<Subjects> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SubjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.MyViewHolder holder, int position) {
        //holder.profileImage.setImageResource(R.drawable.cloud);
        holder.profileImage.setImageResource(list.get(position).getImageProfile());
        holder.subjectName.setText(list.get(position).getSubjectName());
        holder.subjectCode.setText(list.get(position).getSubjectCode());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra("code",list.get(position).getSubjectCode());
            intent.putExtra("subjectName",list.get(position).getSubjectName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView subjectName,subjectCode;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.subjectImage);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectCode = itemView.findViewById(R.id.subjectCode);
        }
    }

}
