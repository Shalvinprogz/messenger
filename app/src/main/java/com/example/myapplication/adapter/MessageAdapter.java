package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.MessagesViewBinding;
import com.example.myapplication.models.MessageModel;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<MessageModel> messageList;

    private final String currentUser;

    public MessageAdapter(List<MessageModel> messageList, String currentUser) {
        this.messageList = messageList;
        this.currentUser = currentUser;
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
    // ViewHolder class
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final MessagesViewBinding binding;

        public MessageViewHolder(MessagesViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MessageModel messageModel) {
            boolean isCurrentUser = messageModel.getUsername().equals(currentUser);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.messageContainer);

            if (!isCurrentUser) {
                constraintSet.clear(R.id.bubble_background, ConstraintSet.START);
                constraintSet.connect(R.id.bubble_background, ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);

                constraintSet.clear(R.id.username, ConstraintSet.START);
                constraintSet.connect(R.id.username, ConstraintSet.END,
                        R.id.bubble_background, ConstraintSet.END, 12);

                constraintSet.clear(R.id.message, ConstraintSet.START);
                constraintSet.connect(R.id.message, ConstraintSet.END,
                        R.id.bubble_background, ConstraintSet.END, 12);

                // Change bubble color for current user (optional)
//                binding.bubbleBackground.setCardBackgroundColor(
//                        ContextCompat.getColor(binding.getRoot().getContext(), R.color.user_message_color));
            } else {
                constraintSet.clear(R.id.bubble_background, ConstraintSet.END);
                constraintSet.connect(R.id.bubble_background, ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);

                // Keep text views aligned to the start of bubble
                constraintSet.clear(R.id.username, ConstraintSet.END);
                constraintSet.connect(R.id.username, ConstraintSet.START,
                        R.id.bubble_background, ConstraintSet.START, 12);

                constraintSet.clear(R.id.message, ConstraintSet.END);
                constraintSet.connect(R.id.message, ConstraintSet.START,
                        R.id.bubble_background, ConstraintSet.START, 12);

//                binding.bubbleBackground.setCardBackgroundColor(
//                        ContextCompat.getColor(binding.getRoot().getContext(), R.color.other_message_color));
            }

            constraintSet.applyTo(binding.messageContainer);

            binding.username.setText(messageModel.getUsername());
            binding.message.setText(messageModel.getMessage());

            // Show/hide username based on user preference
            binding.username.setVisibility(!isCurrentUser ? View.GONE : View.VISIBLE);
        }
    }
}
