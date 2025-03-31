package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentChatDetailBinding;

public class ChatDetailFragment extends Fragment {

    private static final String ARG_CHAT_ID = "chat_id";
    private static final String ARG_CHAT_NAME = "chat_name";

    private FragmentChatDetailBinding binding;
    private long chatId;
    private String chatName;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    public static ChatDetailFragment newInstance(long chatId, String chatName) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHAT_ID, chatId);
        args.putString(ARG_CHAT_NAME, chatName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatId = getArguments().getLong(ARG_CHAT_ID);
            chatName = getArguments().getString(ARG_CHAT_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the toolbar with chat name
        binding.toolbar.setTitle(chatName);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // Set up send button
        binding.btnSend.setOnClickListener(v -> {
            String message = binding.etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // Send message (in a real app, this would use your messaging service)
                // For now, just clear the input field
                binding.etMessage.setText("");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
