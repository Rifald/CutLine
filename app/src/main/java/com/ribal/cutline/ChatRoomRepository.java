package com.ribal.cutline;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class ChatRoomRepository {
    private static final String TAG = "ChatRoomRepo";

    private FirebaseFirestore db;

    public ChatRoomRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public void createRoom(String id_usaha, String id_user,
                           final OnSuccessListener<DocumentReference> successCallback,
                           final OnFailureListener failureCallback) {
        Map<String, Object> room = new HashMap<>();
        room.put("id_usaha", id_usaha);
        room.put("id_user", id_user);
        db.collection("rooms")
                .add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

    public void getChats(String roomId, EventListener<QuerySnapshot> listener) {
        db.collection("chats")
                .whereEqualTo("chat_room_id", roomId)
                .orderBy("sent", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }


    public void getRoomsUser(String id, EventListener<QuerySnapshot> listener) {
        db.collection("rooms")
                .whereEqualTo("id_user", id)
                .addSnapshotListener(listener);
    }

    public void getRoomsBarber(String id, EventListener<QuerySnapshot> listener) {
        db.collection("rooms")
                .whereEqualTo("id_usaha", id)
                .addSnapshotListener(listener);
    }

    public void addMessageToChatRoom(String roomId,
                                     String senderId,
                                     String message,
                                     String time,
                                     final OnSuccessListener<DocumentReference> successCallback,
                                     final OnFailureListener failureCallback) {
        Map<String, Object> chat = new HashMap<>();
        chat.put("chat_room_id", roomId);
        chat.put("sender_id", senderId);
        chat.put("message", message);
        chat.put("time", time);
        chat.put("sent", System.currentTimeMillis());

        db.collection("chats")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

}
