package com.ribal.cutline.activity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationDetailCompleteActivity extends AppCompatActivity {

    private static final String TAG = "Error";
    TextView nama, alamat, contact, tanggal, waktu, status;
    EditText catatan;
    ImageView img_prof;
    Button batalbtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;
    private String idUsaha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
        Intent data = getIntent();
        final String id = data.getStringExtra("id");

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Barber");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        batalbtn = findViewById(R.id.batalkbtn);
        catatan = findViewById(R.id.noteET);

        nama = findViewById(R.id.nama_usahanya);
        alamat = findViewById(R.id.alamat_usaha);
        status = findViewById(R.id.status_tv);
        contact = findViewById(R.id.contact_usaha);
        img_prof = findViewById(R.id.img_profile);
        tanggal = findViewById(R.id.date_tv);
        waktu = findViewById(R.id.waktu_tv);


        db.collection("history").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String statusP = documentSnapshot.getString("status");
                    if (statusP.equals("Pesanan Dibatalkan") || statusP.equals("Ditolak") || statusP.equals("Selesai")) {
                        batalbtn.setVisibility(View.GONE);
                        catatan.setEnabled(false);
                    }
                    idUsaha = documentSnapshot.getString("usahaid");
                    waktu.setText(documentSnapshot.getString("waktu"));
                    tanggal.setText(documentSnapshot.getString("tanggal"));
                    contact.setText(documentSnapshot.get("contact").toString());
                    status.setText(statusP);
                    catatan.setText(documentSnapshot.getString("catatan"));
                    db.collection("barber").document(idUsaha).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                nama.setText(documentSnapshot.getString("nama"));
                                alamat.setText(documentSnapshot.getString("alamat"));
                                final StorageReference Ref = storageReference.child(documentSnapshot.getString("nama") + "/Profile.jpg");
                                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                                .centerCrop().into(img_prof);

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }
}