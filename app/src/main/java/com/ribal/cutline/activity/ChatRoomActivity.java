package com.ribal.cutline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ribal.cutline.Adapter.ChatRoomsAdapter;
import com.ribal.cutline.ChatRoomRepository;
import com.ribal.cutline.R;
import com.ribal.cutline.model.ChatRoom;


import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
    public static final String CHAT_ROOM_USER_ID = "CHAT_ROOM_USER_ID";
    public static final String CHAT_ROOM_BARBER_ID = "CHAT_ROOM_BARBER_ID";

    private String roomId = "";
    private String roomName = "";

    ChatRoomRepository chatRoomRepository;
    private RecyclerView chatRooms;
    private ChatRoomsAdapter adapter;

    FirebaseAuth fAuth;
    String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());
        initUI();

        db = FirebaseFirestore.getInstance();

        getChatRoomUsers();
    }

    private void getChatRoomUsers() {
        chatRoomRepository.getRoomsUser(userId, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("MainActivity", "Listen failed.", e);
                    return;
                }

                List<ChatRoom> rooms = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    rooms.add(new ChatRoom(doc.getId(), doc.getString("id_usaha"), doc.getString("id_user")));
                }

                adapter = new ChatRoomsAdapter(rooms, listener);
                chatRooms.setAdapter(adapter);
            }
        });
    }

    private void getChatRoomBarbers() {
        chatRoomRepository.getRoomsBarber(userId, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("MainActivity", "Listen failed.", e);
                    return;
                }

                List<ChatRoom> rooms = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    rooms.add(new ChatRoom(doc.getId(), doc.getString("id_usaha"), doc.getString("id_user")));
                }

                adapter = new ChatRoomsAdapter(rooms, listener);
                chatRooms.setAdapter(adapter);
            }
        });
    }

    ChatRoomsAdapter.OnChatRoomClickListener listener = new ChatRoomsAdapter.OnChatRoomClickListener() {
        @Override
        public void onClick(ChatRoom chatRoom) {
            Intent intent = new Intent(ChatRoomActivity.this, ChatActivity.class);
            intent.putExtra(ChatRoomActivity.CHAT_ROOM_ID, chatRoom.getId());
            intent.putExtra(ChatRoomActivity.CHAT_ROOM_USER_ID, chatRoom.getId_user());
            intent.putExtra(ChatRoomActivity.CHAT_ROOM_BARBER_ID, chatRoom.getId_usaha());
            startActivity(intent);
        }
    };

    private void initUI() {
        chatRooms = findViewById(R.id.rooms);
        chatRooms.setLayoutManager(new LinearLayoutManager(this));
    }
}