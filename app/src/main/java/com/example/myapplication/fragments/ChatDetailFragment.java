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
import com.example.myapplication.adapter.MessageAdapter;
import com.example.myapplication.client.MessageClient;
import com.example.myapplication.databinding.FragmentChatDetailBinding;
import com.example.myapplication.models.MessageDTO;
import com.example.myapplication.models.MessageModel;
import com.google.android.material.expandable.ExpandableWidget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class ChatDetailFragment extends Fragment {

    private static final String USERNAME = "username";
    private static final String ARG_CHAT_NAME = "chat_name";

    private static final String CONV_ID = "conversationId";

    private FragmentChatDetailBinding binding;
    private String username;

    private Long conversationId;
    private String chatName;

    private MessageAdapter messageAdapter;

    private List<MessageModel> messageList;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    public static ChatDetailFragment newInstance(String username, String chatName, Long conversationId) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(ARG_CHAT_NAME, chatName);
        args.putLong(CONV_ID, conversationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            chatName = getArguments().getString(ARG_CHAT_NAME);
            conversationId = getArguments().getLong(CONV_ID);
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

        binding.toolbar.setTitle(chatName);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
            binding.getRoot().setVisibility(View.GONE);

        });
        setupRecyclerView();


        // Set up send button
        binding.btnSend.setOnClickListener(v -> {
            String message = Objects.requireNonNull(binding.etMessage.getText()).toString().trim();
            if (!message.isEmpty()) {
                // Send message (in a real app, this would use your messaging service)
                // For now, just clear the input field
                binding.etMessage.setText("");
            }
        });

        try {
            loadMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, username);
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerMessages.setAdapter(messageAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMessages() throws IOException {
        for (int i = 1; i <= 5; i++) {
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Sample message " + i);
            messageModel.setUsername("user" + i);
            messageList.add(messageModel);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<MessageModel> messageDTOS;
            try {
                messageDTOS = MessageClient.getInstance().getAllMessages(conversationId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<MessageModel> finalMessageDTOS = messageDTOS;
            requireActivity().runOnUiThread(() -> {
                messageList.addAll(finalMessageDTOS);
                messageAdapter.notifyDataSetChanged();
            });

        });
    }

}
