package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MessageAdapter;
import com.example.myapplication.client.MessageClient;
import com.example.myapplication.databinding.FragmentChatDetailBinding;
import com.example.myapplication.enums.SocketMessageType;
import com.example.myapplication.models.MessageDTO;
import com.example.myapplication.models.MessageModel;
import com.example.myapplication.models.SocketMessageDTO;
import com.example.myapplication.socket.SocketService;
import com.example.myapplication.util.HttpUtil;
import com.google.android.material.expandable.ExpandableWidget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class ChatDetailFragment extends Fragment implements SocketService.MessageListener {

    private static final String USERNAME = "username";

    private static final String TO_USER = "receiver";
    private static final String ARG_CHAT_NAME = "chat_name";

    private static final String CONV_ID = "conversationId";

    private FragmentChatDetailBinding binding;
    private String username;

    private String toUser;

    private Long conversationId;
    private String chatName;

    private MessageAdapter messageAdapter;

    private List<MessageModel> messageList;

    private SocketService socketService;

    private boolean isBound = false;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            socketService = binder.getService();
            socketService.addMessageListener(ChatDetailFragment.this);
            isBound = true;

            if (socketService.isConnected()) {
                Toast.makeText(requireContext(), "Forgot password clicked", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static ChatDetailFragment newInstance(String username, String chatName, Long conversationId, String toUser) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(ARG_CHAT_NAME, chatName);
        args.putLong(CONV_ID, conversationId);
        args.putString(TO_USER, toUser);
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
            toUser = getArguments().getString(TO_USER);
        }
        Intent intent = new Intent(requireContext(), SocketService.class);
        requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbar.setTitle(chatName);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
            binding.getRoot().setVisibility(View.GONE);

        });
        setupRecyclerView();


        binding.btnSend.setOnClickListener(v -> {
            String message = Objects.requireNonNull(binding.etMessage.getText()).toString().trim();
            if (!message.isEmpty()) {
                MessageModel messageModel = new MessageModel();
                messageModel.setMessage(message);
                messageModel.setTimestamp(new Date());
                messageModel.setUsername(username);
                messageList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                socketService.sendMessage(getSocketMessage());
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

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void onConnectionStatusChanged(boolean connected) {

    }

    private String getSocketMessage() {
        String text = Objects.requireNonNull(binding.etMessage.getText()).toString();
        SocketMessageDTO socketMessageDTO = new SocketMessageDTO();
        socketMessageDTO.setMessageType(SocketMessageType.PERSONAL_CHAT);
        socketMessageDTO.setTimestamp(new Date());
        socketMessageDTO.setConvId(conversationId);
        socketMessageDTO.setUsername(username);
        socketMessageDTO.setToUser(toUser);
        socketMessageDTO.setMessage(text);
        return HttpUtil.getInstance().getGson().toJson(socketMessageDTO);
    }
}
