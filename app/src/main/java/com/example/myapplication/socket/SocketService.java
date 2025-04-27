package com.example.myapplication.socket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class SocketService extends Service {
    private static final String TAG = "SocketService";
    private static final String CHANNEL_ID = "SocketServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private StompClient stompClient;
    private CompositeDisposable compositeDisposable;
    private final IBinder binder = new LocalBinder();
    private boolean isConnected = false;
    private final Handler reconnectHandler = new Handler(Looper.getMainLooper());
    private final int RECONNECT_DELAY = 5000; // 5 seconds

    private final List<MessageListener> messageListeners = new ArrayList<>();

    public interface MessageListener {
        void onMessageReceived(String message);
        void onConnectionStatusChanged(boolean connected);
    }

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        compositeDisposable = new CompositeDisposable();
        connectStompClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Restart service if killed
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        closeStompConnection();
        super.onDestroy();
    }

    // Connect to the STOMP server
    private void connectStompClient() {
        String url = "ws://10.0.2.2:8888/messenger/ws";

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);

        // Configure optional settings
        stompClient.withClientHeartbeat(10000).withServerHeartbeat(10000);

        // Subscribe to lifecycle events
        compositeDisposable.add(stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "STOMP connection opened");
                            isConnected = true;
                            notifyConnectionStatus(true);
                            break;
                        case CLOSED:
                            Log.d(TAG, "STOMP connection closed");
                            isConnected = false;
                            notifyConnectionStatus(false);
                            scheduleReconnect();
                            break;
                        case ERROR:
                            Log.e(TAG, "STOMP connection error", lifecycleEvent.getException());
                            isConnected = false;
                            notifyConnectionStatus(false);
                            scheduleReconnect();
                            break;
                    }
                }));

        // Connect to the server
        stompClient.connect();

        // Subscribe to message topics
        subscribeToTopics();
    }

    private void subscribeToTopics() {
        // Subscribe to general messages topic

        compositeDisposable.add(stompClient.topic("/topic/messages")
                .subscribeOn(Schedulers.io())
                .subscribe(topicMessage -> {
                    String message = topicMessage.getPayload();
                    Log.d(TAG, "Received message: " + message);
                    notifyMessageReceived(message);
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe to topic", throwable);
                }));

        // Add more topic subscriptions as needed
        // For example, user-specific messages
        // compositeDisposable.add(stompClient.topic("/user/queue/messages")...
    }

    // Schedule a reconnection attempt
    private void scheduleReconnect() {
        reconnectHandler.removeCallbacksAndMessages(null);
        reconnectHandler.postDelayed(this::connectStompClient, RECONNECT_DELAY);
    }

    private void closeStompConnection() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        if (stompClient != null) {
            stompClient.disconnect();
        }

        reconnectHandler.removeCallbacksAndMessages(null);
    }

    // Send a message to a specific destination
    public void sendMessage(String destination, String message) {
        if (stompClient != null && isConnected) {
            List<StompHeader> headers = new ArrayList<>();
            headers.add(new StompHeader("content-type", "text/plain"));

            // Make sure destination starts with '/app' since that's your application prefix
            String fullDestination = destination.startsWith("/app") ? destination : "/app" + destination;
            StompMessage stompMessage = new StompMessage(fullDestination,headers, message);


            compositeDisposable.add(stompClient.send(fullDestination, message)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            () -> Log.d(TAG, "Message sent successfully to: " + fullDestination),
                            throwable -> Log.e(TAG, "Error sending message to: " + fullDestination, throwable)
                    ));
        } else {
            Log.e(TAG, "Cannot send message. STOMP client is not connected.");
        }
    }

    // Helper method for sending to chat.register endpoint
    public void registerUser(String username) {
        sendMessage("/chat.register", username);
    }

    // For backward compatibility with your previous implementation
    public void sendMessage(String message) {
        sendMessage("/chat.message", message);
    }

    public void addMessageListener(MessageListener listener) {
        if (!messageListeners.contains(listener)) {
            messageListeners.add(listener);
            // Immediately notify new listener of current connection status
            listener.onConnectionStatusChanged(isConnected);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    private void notifyMessageReceived(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (MessageListener listener : messageListeners) {
                listener.onMessageReceived(message);
            }
        });
    }

    private void notifyConnectionStatus(boolean connected) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (MessageListener listener : messageListeners) {
                listener.onConnectionStatusChanged(connected);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Socket Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Channel for socket service notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Create the notification for foreground service
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Chat Service")
                .setContentText("Maintaining chat connection")
                .setSmallIcon(R.drawable.ic_chat_logo)
                .setContentIntent(pendingIntent)
                .build();
    }

    public boolean isConnected() {
        return isConnected;
    }
}