package com.ribal.cutline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FragmentProfile extends Fragment {

    TextView namaTv,alamatTv,emailTv;
    ImageView profileIv;
    private ProgressBar pb;
    private Button logBt, editBt;
    RelativeLayout rv_data;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    String userId;

    public FragmentProfile() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        namaTv = view.findViewById(R.id.nama_tv);
        alamatTv = view.findViewById(R.id.alamat_tv);
        emailTv = view.findViewById(R.id.email_tv);
        profileIv = view.findViewById(R.id.profileImageView);
        pb = view.findViewById(R.id.progressBar);
        logBt = view.findViewById(R.id.logout_btn);
        editBt = view.findViewById(R.id.editProfile_btn);
        rv_data = view.findViewById(R.id.relative_data);

        storageReference = FirebaseStorage.getInstance().getReference();
        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        rv_data.setVisibility(View.GONE);
        logBt.setVisibility(View.GONE);
        editBt.setVisibility(View.GONE);
        profileIv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        DocumentReference docRef = fstore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                namaTv.setText(documentSnapshot.getString("nama"));
                alamatTv.setText(documentSnapshot.getString("alamat"));
                emailTv.setText(documentSnapshot.getString("email"));
                showImage();
                display();
            }
        });

        logBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), Login.class));
                    getActivity().finish();

            }
        });

        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("nama",namaTv.getText().toString());
                i.putExtra("email",emailTv.getText().toString());
                i.putExtra("alamat",alamatTv.getText().toString());
                startActivity(i);

            }
        });
    }

    private void showImage() {
        StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                        .centerCrop().into(profileIv);

            }
        });
    }

    private void display() {
        logBt.setVisibility(View.VISIBLE);
        editBt.setVisibility(View.VISIBLE);
        profileIv.setVisibility(View.VISIBLE);
        rv_data.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
    }
}