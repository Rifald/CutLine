package com.ribal.cutline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
    public static final String CHAT_ROOM_NAME = "CHAT_ROOM_NAME";

    private String roomId = "";
    private String roomName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomId = extras.getString(CHAT_ROOM_ID, "");
            roomName = extras.getString(CHAT_ROOM_NAME, "");
        }


    }
}