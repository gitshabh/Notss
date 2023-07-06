package com.example.notesharingminiprojectapp;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private Context context;
    private List<MessageModel> messageModelList;

    public MessageAdapter(Context context) {
        this.context = context;
        messageModelList = new ArrayList<>();
    }

    public void add(MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    public void clear() {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.receiverMessage.setVisibility(View.INVISIBLE);
        holder.senderMessage.setVisibility(View.INVISIBLE);
        holder.senderFile.setVisibility(View.INVISIBLE);
        holder.receiverFile.setVisibility(View.INVISIBLE);
        holder.receiverMessageLayout.setVisibility(View.INVISIBLE);

        if(messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {

            holder.receiverMessageLayout.setVisibility(View.GONE);
            holder.receiverFile.setVisibility(View.GONE);

            if(messageModel.getMsgType().equals("file")) {
                holder.senderFile.setVisibility(View.VISIBLE);
                holder.senderFileName.setText(messageModel.getFileName());
            } else {
                holder.senderMessage.setVisibility(View.VISIBLE);
                holder.senderMessage.setText(messageModel.getMessage());
            }

        } else {
            if(messageModel.getMsgType().equals("file")) {
                holder.receiverMessageLayout.setVisibility(View.GONE);

                holder.receiverFile.setVisibility(View.VISIBLE);
                holder.fileReceiverName.setText(messageModel.getSenderName());
                holder.receiverFileName.setText(messageModel.getFileName());
            } else {
                holder.receiverFile.setVisibility(View.GONE);

                holder.receiverMessageLayout.setVisibility(View.VISIBLE);
                holder.receiverName.setText(messageModel.getSenderName());
                holder.receiverMessage.setVisibility(View.VISIBLE);
                holder.receiverMessage.setText(messageModel.getMessage());
            }
        }
        holder.itemView.setOnClickListener(view -> {
            if(messageModel.getMsgType().equals("file")) {
                Toast.makeText(context, "Download Started. Please wait...", Toast.LENGTH_LONG).show();
                DownloadManager manager = (DownloadManager) holder.itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(messageModel.getMessage());
                DownloadManager.Request request = new DownloadManager.Request(uri).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,messageModel.getFileName());
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                long reference = manager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView senderMessage,receiverMessage,fileReceiverName,receiverName,senderFileName,receiverFileName;
        private LinearLayout senderFile,receiverFile,receiverMessageLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderMessage);
            receiverMessage = itemView.findViewById(R.id.receiverMessage);
            senderFile = itemView.findViewById(R.id.senderFile);
            receiverFile = itemView.findViewById(R.id.receiverFile);
            receiverMessageLayout = itemView.findViewById(R.id.receiverMessageLayout);
            fileReceiverName = itemView.findViewById(R.id.fileReceiverName);
            receiverName = itemView.findViewById(R.id.receiverName);
            senderFileName = itemView.findViewById(R.id.senderFileName);
            receiverFileName = itemView.findViewById(R.id.receiverFileName);
        }
    }
}
