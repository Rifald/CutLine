package com.ribal.cutline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {

    private static final String TAG = "Error";
    TextView nama, alamat, contact, tanggal, waktu;
    ImageView img_prof;
    Button pesanbtn, tanggalbtn, waktubtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;
    TimePickerDialog Tpicker;
    DatePickerDialog Dpicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent data = getIntent();
        final String id = data.getStringExtra("id");

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Barber");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        pesanbtn = findViewById(R.id.pesanbtn);
        tanggalbtn = findViewById(R.id.datebtn);
        waktubtn= findViewById(R.id.timebtn);

        nama = findViewById(R.id.nama_usahanya);
        alamat = findViewById(R.id.alamat_usaha);
        contact = findViewById(R.id.contact_usaha);
        img_prof = findViewById(R.id.img_profile);
        tanggal = findViewById(R.id.date_tv);
        waktu = findViewById(R.id.waktu_tv);

        db.collection("barber").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    nama.setText(documentSnapshot.getString("nama"));
                    alamat.setText(documentSnapshot.getString("alamat"));
                    contact.setText(documentSnapshot.get("contact").toString());
                    final StorageReference Ref = storageReference.child(documentSnapshot.getString("nama")  + "/Profile.jpg");
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

        waktubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog

                Tpicker = new TimePickerDialog(ReservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                waktu.setText(String.format("%02d:%02d", sHour, sMinute));
                            }
                        }, hour, minutes, true);

                Tpicker.show();
            }
        });

        tanggalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                Dpicker = new DatePickerDialog(ReservationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tanggal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                Dpicker.show();
            }
        });
        pesanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaU = nama.getText().toString().trim();
                String alamatU = alamat.getText().toString().trim();
                String contactU = contact.getText().toString().trim();



                final ProgressDialog progressDialog = new ProgressDialog(ReservationActivity.this);

                progressDialog.setMessage("Sedang di proses...");
                progressDialog.show();

                Map<String, Object> file = new HashMap<>();
                file.put("usahaid", id);
                file.put("userid", userId);
                file.put("nama", namaU);
                file.put("contact", contactU);
                file.put("alamat", alamatU);
                file.put("tanggal", tanggal.getText());
                file.put("waktu", waktu.getText());
                file.put("status", "Sedang diproses");
                file.put("catatan", "");
                db.collection("pesan").document(userId)
                        .set(file)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(ReservationActivity.this, "Pesanan Diproses", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });


    }
}