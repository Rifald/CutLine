package com.ribal.cutline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.Adapter.ViewPageAdapter;
import com.ribal.cutline.Interface.iFireStoreLoadDone;
import com.ribal.cutline.R;
import com.ribal.cutline.model.ImagePage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ManageBusinessActivity extends AppCompatActivity implements com.ribal.cutline.Interface.iFireStoreLoadDone {
    private static final String TAG = "Error";
    TextView nama, desc, alamat, harga, contact;
    ImageView img_prof;
    Button editbtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FloatingActionButton floatingActionButton;
    FirebaseAuth fAuth;
    String userId;

    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;

    iFireStoreLoadDone iFireStoreLoadDone;
    CollectionReference img_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Barber");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        editbtn = findViewById(R.id.edit_btn);
        nama = findViewById(R.id.nama_usahanya);
        desc = findViewById(R.id.desc_usaha);
        alamat = findViewById(R.id.alamat_usaha);
        harga = findViewById(R.id.harga_usaha);
        contact = findViewById(R.id.contact_usaha);
        img_prof = findViewById(R.id.img_profile);
        floatingActionButton= findViewById(R.id.float_btn);
        editbtn = findViewById(R.id.edit_btn);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),AddImageBusiness.class);
                i.putExtra("id",userId);
                startActivity(i);
            }
        });

        db.collection("barber").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {

                    nama.setText(documentSnapshot.getString("nama"));
                    alamat.setText(documentSnapshot.getString("alamat"));
                    desc.setText(documentSnapshot.getString("desc"));
                    harga.setText(documentSnapshot.get("harga").toString());
                    contact.setText(documentSnapshot.getString("contact"));
                    final StorageReference Ref = storageReference.child(nama.getText().toString().trim()  + "/Profile.jpg");
                    Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                    .centerCrop().into(img_prof);

                        }
                    });
                }else{
                    startActivity(new Intent(ManageBusinessActivity.this, CreateBusinessActivity.class));
                    finish();
                }
            }


        });

        //init
        iFireStoreLoadDone = this;
        img_page = FirebaseFirestore.getInstance().collection("barber").document(userId).collection("image");
        viewPager = findViewById(R.id.img_VP);

        getData();

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),CreateBusinessActivity.class);
                i.putExtra("idU",userId);
                startActivity(i);
                finish();

            }
        });

    }

    private void getData() {
        img_page.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iFireStoreLoadDone.onFireStoreLoadFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<ImagePage> imagePages = new ArrayList<>();
                    for (QueryDocumentSnapshot imageSnapshot : task.getResult()){
                        ImagePage imagePage = imageSnapshot.toObject(ImagePage.class);
                        imagePages.add(imagePage);
                    }
                    iFireStoreLoadDone.onFireStoreLoadSuccess(imagePages);
                }
            }
        });
    }

    @Override
    public void onFireStoreLoadSuccess(List<ImagePage> imagePages) {
        viewPagerAdapter = new ViewPageAdapter(this, imagePages);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onFireStoreLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}