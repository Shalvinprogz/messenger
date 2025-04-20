package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.client.MessageClient;
import com.example.myapplication.databinding.HomePageBinding;
import com.example.myapplication.models.HomeChatModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private HomePageBinding binding;
    private ChatAdapter chatAdapter;
    private List<HomeChatModel> chatList;

    private static String username;

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
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupClickListeners();
        loadData();
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
    private void loadData() {
        chatList.add(new HomeChatModel("shalvin", "John Doe", "Hey, how are you doing?", "10:30 AM", 0));
        chatList.add(new HomeChatModel("Hindetougk", "Jane Smith", "Let's meet tomorrow", "Yesterday", 2));
        chatList.add(new HomeChatModel("gmvjhv", "Mike Johnson", "Did you see that new movie?", "Yesterday", 0));
        chatList.add(new HomeChatModel("yjjbkjbkj", "Sarah Williams", "Can you help me with the project?", "Monday", 5));
        chatList.add(new HomeChatModel("ykvkkkjkkjb", "David Brown", "Meeting at 2 PM", "05/12/2024", 0));


        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<HomeChatModel> messages = MessageClient.getInstance().getHomePageMessages(username);

                requireActivity().runOnUiThread(() -> {

                    if (messages != null && !messages.isEmpty()) {
                        chatList.addAll(messages);
                        chatAdapter.notifyDataSetChanged();
                    }

                    if (chatList.isEmpty()) {
                        binding.tvEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvEmptyState.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

                requireActivity().runOnUiThread(() -> {

                    Toast.makeText(requireContext(),
                            "Failed to load messages: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    if (chatList.isEmpty()) {
                        binding.tvEmptyState.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onChatItemClick(HomeChatModel chat) {

        chat.setUnreadCount(0);
        chatAdapter.notifyDataSetChanged();

        binding.getRoot().setVisibility(View.GONE);

        ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance(chat.getUsername(),
                chat.getName(), Long.valueOf(chat.getConversationId()));
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