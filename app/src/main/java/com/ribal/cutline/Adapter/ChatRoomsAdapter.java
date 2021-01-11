package com.ribal.cutline.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.ribal.cutline.model.ChatRoom;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder> {

    public interface OnChatRoomClickListener {
        void onClick(ChatRoom chatRoom);
    }

    private OnChatRoomClickListener listener;

    private List<ChatRoom> chatRooms;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ChatRoomsAdapter(List<ChatRoom> chatRooms, OnChatRoomClickListener listener) {
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_chat_room,
                parent,
                false
        );
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {


        holder.bind(chatRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ChatRoom chatRoom;
        ImageView imageView;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
            imageView = itemView.findViewById(R.id.profileImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(chatRoom);
                }
            });
        }

        public void bind(ChatRoom chatRoom) {
            this.chatRoom = chatRoom;
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            String userId = fAuth.getCurrentUser().getUid();
            if (chatRoom.getId_user().equals(userId)) {
                db.collection("barber").document(chatRoom.getId_usaha()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String nama = documentSnapshot.getString("nama");
                        name.setText(nama);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Barber");
                        final StorageReference Ref = storageReference.child(nama  + "/Profile.jpg");
                        Log.d("Error", Ref+"");
                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                        .fit().centerCrop().into(imageView);
                            }
                        });
                    }
                });
            }
            else if (chatRoom.getId_usaha().equals(userId)) {
                db.collection("users").document(chatRoom.getId_user()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.getString("nama"));

                    }
                });
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("users");
                final StorageReference Ref = storageReference.child(chatRoom.getId_user() + "/profile.jpg");
                Log.d("Error", Ref+"");
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                .fit().centerCrop().into(imageView);
                    }
                });
            }
        }
    }
}
