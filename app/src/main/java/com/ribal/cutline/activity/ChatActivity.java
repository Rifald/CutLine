package com.ribal.cutline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ribal.cutline.Adapter.ChatsAdapter;
import com.ribal.cutline.ChatRoomRepository;
import com.ribal.cutline.R;
import com.ribal.cutline.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
    public static final String CHAT_ROOM_USER_ID = "CHAT_ROOM_USER_ID";
    public static final String CHAT_ROOM_BARBER_ID = "CHAT_ROOM_BARBER_ID";

    private String roomId = "";
    private String roomUserId = "";
    private String roomBarberId = "";
    String time ="";
    FirebaseAuth fAuth;
    String userId;

    private ChatRoomRepository chatRoomRepository;
    private FirebaseFirestore db;

    private EditText message;
    private ImageButton send;
    private RecyclerView chats;
    private TextView name;
    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = findViewById(R.id.nama_chat);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomId = extras.getString(CHAT_ROOM_ID, "");
            roomUserId = extras.getString(CHAT_ROOM_USER_ID, "");
            roomBarberId = extras.getString(CHAT_ROOM_BARBER_ID, "");
        }
        if(userId.equals(roomUserId)){
            db.collection("barber").document(roomBarberId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        name.setText(documentSnapshot.getString("nama"));
                    }
                }
            });
        }else if(userId.equals(roomBarberId)){
            db.collection("users").document(roomUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        name.setText(documentSnapshot.getString("nama"));

                    }
                }
            });
        }




        initUI();

        showChatMessages();
    }


    private void initUI() {
        message = findViewById(R.id.message_text);
        send = findViewById(R.id.send_message);
        chats = findViewById(R.id.chats);
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:mm a");

        time = date.format(currentTime);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chats.setLayoutManager(manager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(
                            ChatActivity.this,
                            "Please enter a message",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    addMessageToChatRoom();
                }
            }
        });
    }


    private void addMessageToChatRoom() {
        String chatMessage = message.getText().toString();
        message.setText("");
        send.setEnabled(false);
        chatRoomRepository.addMessageToChatRoom(
                roomId,
                userId,
                chatMessage,
                time,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        send.setEnabled(true);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        send.setEnabled(true);
                        Toast.makeText(
                                ChatActivity.this,
                                "Failed to send your message",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void showChatMessages() {
        chatRoomRepository.getChats(roomId, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ChatRoomActivity", "Listen failed.", e);
                    return;
                }

                List<Chat> messages = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    messages.add(
                            new Chat(
                                    doc.getId(),
                                    doc.getString("chat_room_id"),
                                    doc.getString("sender_id"),
                                    doc.getString("message"),
                                    doc.getString("time"),
                                    doc.getLong("sent")
                            )
                    );
                }

                adapter = new ChatsAdapter(messages, userId);
                chats.setAdapter(adapter);
            }
        });
    }
}