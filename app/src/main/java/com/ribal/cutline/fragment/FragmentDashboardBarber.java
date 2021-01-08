package com.ribal.cutline.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.ribal.cutline.activity.ChatRoomBarberActivity;
import com.ribal.cutline.activity.ManageBusinessActivity;
import com.ribal.cutline.activity.OrderRequestActivity;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentDashboardBarber extends Fragment {

    Button manageB,reserveB,chatRoom;
    TextView namaTv,alamatTv;
    ImageView profileIv;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    String userId;

    public FragmentDashboardBarber() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_barber, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        namaTv = view.findViewById(R.id.nama_user);
        alamatTv = view.findViewById(R.id.alamat_user);
        profileIv = view.findViewById(R.id.profileImageView);

        manageB = view.findViewById(R.id.manage_btn);
        reserveB = view.findViewById(R.id.reserve_btn);
        chatRoom = view.findViewById(R.id.chat_btn);

        storageReference = FirebaseStorage.getInstance().getReference();
        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = fstore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                namaTv.setText(documentSnapshot.getString("nama"));
                alamatTv.setText(documentSnapshot.getString("alamat"));
                showImage();
            }

            private void showImage() {
                StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit()
                                .centerCrop().into(profileIv);

                    }
                });
            }
        });

        manageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ManageBusinessActivity.class));
            }
        });
        reserveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), OrderRequestActivity.class));
            }
        });

        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatRoomBarberActivity.class));
            }
        });
    }
}