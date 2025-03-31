package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemChatBinding;
import com.example.myapplication.models.ChatModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatModel> chatList;
    private final OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(ChatModel chat);
    }

    public ChatAdapter(List<ChatModel> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatModel chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBinding binding;

        ChatViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatModel chat) {
            binding.tvName.setText(chat.getName());
            binding.tvLastMessage.setText(chat.getLastMessage());
            binding.tvTime.setText(chat.getTime());

            // Set unread count badge
            if (chat.getUnreadCount() > 0) {
                binding.badgeUnread.setVisibility(View.VISIBLE);
                binding.badgeUnread.setText(String.valueOf(chat.getUnreadCount()));
            } else {
                binding.badgeUnread.setVisibility(View.GONE);
            }

            // Set click listener
            itemView.setOnClickListener(v -> listener.onChatClick(chat));
        }
    }
}
