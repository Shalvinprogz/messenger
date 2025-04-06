package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentChatDetailBinding;
import com.example.myapplication.databinding.MessagesViewBinding;
import com.example.myapplication.models.MessageModel;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<MessageModel> messageList;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessagesViewBinding binding = MessagesViewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageAdapter.MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final MessagesViewBinding binding;

        public MessageViewHolder(MessagesViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MessageModel message) {
            binding.username.setText(message.getUsername());
            binding.message.setText(message.getMessage());
        }
    }
}
