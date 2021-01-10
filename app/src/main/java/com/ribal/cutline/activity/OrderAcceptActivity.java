package com.ribal.cutline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OrderAcceptActivity extends AppCompatActivity {
    private static final String TAG = "Error";
    TextView nama, alamat, contact, tanggal, waktu;
    EditText catatan;
    ImageView img_prof;
    Button batalbtn, selesaibtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;
    LinearLayout ln;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);

        Intent data = getIntent();
        final String id = data.getStringExtra("id");

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("users");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        selesaibtn = findViewById(R.id.selesaibtn);
        batalbtn = findViewById(R.id.batalbtn);
        catatan= findViewById(R.id.noteET);

        nama = findViewById(R.id.nama_usahanya);
        alamat = findViewById(R.id.alamat_usaha);
        contact = findViewById(R.id.contact_usaha);
        img_prof = findViewById(R.id.img_profile);
        tanggal = findViewById(R.id.date_tv);
        waktu = findViewById(R.id.waktu_tv);
        ln = findViewById(R.id.linearLayout2);
        pb = findViewById(R.id.progressBar);

        ln.setVisibility(View.GONE);


        db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    nama.setText(documentSnapshot.getString("nama"));
                    alamat.setText(documentSnapshot.getString("alamat"));
                    final StorageReference Ref = storageReference.child(id + "/profile.jpg");
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

        db.collection("pesan").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    waktu.setText(documentSnapshot.getString("waktu"));
                    tanggal.setText(documentSnapshot.getString("tanggal"));
                    contact.setText(documentSnapshot.get("contact").toString());
                    ln.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);


                }
            }
        });

        batalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(OrderAcceptActivity.this);

                progressDialog.setMessage("Processing...");
                progressDialog.show();

                Map<String, Object> file = new HashMap<>();
                file.put("usahaid", userId);
                file.put("catatan", catatan.getText().toString());
                file.put("userid", id);
                file.put("contact", contact.getText().toString());
                file.put("waktu", waktu.getText().toString());
                file.put("tanggal", tanggal.getText().toString());
                file.put("status", "Dibatalkan");
                file.put("sent", System.currentTimeMillis());

                db.collection("history").document().set(file).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("pesan").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(OrderAcceptActivity.this, "Pesanan Dibatalkan", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
            }
        });

        selesaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(OrderAcceptActivity.this);

                progressDialog.setMessage("Processing...");
                progressDialog.show();

                Map<String, Object> file = new HashMap<>();
                file.put("usahaid", userId);
                file.put("catatan", catatan.getText().toString());
                file.put("userid", id);
                file.put("contact", contact.getText().toString());
                file.put("waktu", waktu.getText().toString());
                file.put("tanggal", tanggal.getText().toString());
                file.put("status", "Selesai");
                file.put("sent", System.currentTimeMillis());

                db.collection("history").document().set(file).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("pesan").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(OrderAcceptActivity.this, "Pesanan Selesai", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });

            }
        });

    }
}