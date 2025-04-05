package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.databinding.HomePageBinding;
import com.example.myapplication.models.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomePageBinding binding;
    private ChatAdapter chatAdapter;
    private List<ChatModel> chatList;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupClickListeners();
        loadDummyData();
    }

    private void setupRecyclerView() {
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList, this::onChatItemClick);
        binding.recyclerChats.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerChats.setAdapter(chatAdapter);
    }

    private void setupClickListeners() {
        binding.fabNewChat.setOnClickListener(v -> {
            // Handle new chat button click
            // You would navigate to a contact selection fragment or dialog
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDummyData() {
        chatList.add(new ChatModel( "s", "John Doe", "Hey, how are you doing?", "10:30 AM", 0));
        chatList.add(new ChatModel("2", "Jane Smith", "Let's meet tomorrow", "Yesterday", 2));
        chatList.add(new ChatModel("3", "Mike Johnson", "Did you see that new movie?", "Yesterday", 0));
        chatList.add(new ChatModel("4", "Sarah Williams", "Can you help me with the project?", "Monday", 5));
        chatList.add(new ChatModel("5", "David Brown", "Meeting at 2 PM", "05/12/2024", 0));

        chatAdapter.notifyDataSetChanged();

        // Show/hide empty state based on data
        if (chatList.isEmpty()) {
            binding.tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            binding.tvEmptyState.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onChatItemClick(ChatModel chat) {
        // Navigate to chat detail fragment
        // Reset unread count
        chat.setUnreadCount(0);
        chatAdapter.notifyDataSetChanged();

        // Example of navigating to chat detail fragment
        ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance(chat.getUsername(), chat.getName());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, chatDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}