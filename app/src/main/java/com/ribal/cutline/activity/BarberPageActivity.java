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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.Adapter.ViewPageAdapter;
import com.ribal.cutline.ChatRoomRepository;
import com.ribal.cutline.R;
import com.ribal.cutline.model.ImagePage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BarberPageActivity extends AppCompatActivity implements com.ribal.cutline.Interface.iFireStoreLoadDone {

    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
    public static final String CHAT_ROOM_USER_ID = "CHAT_ROOM_USER_ID";
    public static final String CHAT_ROOM_BARBER_ID = "CHAT_ROOM_BARBER_ID";

    private static final String TAG = "Error";
    TextView nama, desc, alamat, harga, contact;
    ImageView img_prof;
    Button pesanbtn, chatbtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;

    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;

    private ChatRoomRepository chatRoomRepository;

    com.ribal.cutline.Interface.iFireStoreLoadDone iFireStoreLoadDone;
    CollectionReference img_page;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_page);

        Intent data = getIntent();
        final String id = data.getStringExtra("id");


        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Barber");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        pesanbtn = findViewById(R.id.pesan_btn);
        chatbtn = findViewById(R.id.chat_btn);
        nama = findViewById(R.id.nama_usahanya);
        desc = findViewById(R.id.desc_usaha);
        alamat = findViewById(R.id.alamat_usaha);
        harga = findViewById(R.id.harga_usaha);
        contact = findViewById(R.id.contact_usaha);
        img_prof = findViewById(R.id.img_profile);

        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        db.collection("barber").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    nama.setText(documentSnapshot.getString("nama"));
                    alamat.setText(documentSnapshot.getString("alamat"));
                    desc.setText(documentSnapshot.getString("desc"));
                    harga.setText(documentSnapshot.get("harga").toString());
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
        //init
        iFireStoreLoadDone = this;
        img_page = FirebaseFirestore.getInstance().collection("barber").document(id).collection("image");
        viewPager = findViewById(R.id.img_VP);

        getData();

        pesanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BarberPageActivity.this,ReservationActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });

        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference cref=db.collection("rooms");
                Query q1=cref.whereEqualTo("id_usaha",id).whereEqualTo("id_user",userId);
                q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean isExisting = false;
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            String rId, rUsaha, rUser;
                            rId = ds.getId();
                            rUsaha = ds.getString("id_usaha");
                            rUser = ds.getString("id_user");
                            if(rUsaha.equals(id)){
                                if(rUser.equals(userId)){
                                    isExisting = true;
                                    Intent intent = new Intent(BarberPageActivity.this, ChatActivity.class);
                                    intent.putExtra(BarberPageActivity.CHAT_ROOM_ID, ds.getId());
                                    intent.putExtra(BarberPageActivity.CHAT_ROOM_USER_ID, userId);
                                    intent.putExtra(BarberPageActivity.CHAT_ROOM_BARBER_ID, id);
                                    startActivity(intent);
                                }
                            }


                        }
                        if (!isExisting) {
                            // TODO: add item to Firestore

                            chatRoomRepository.createRoom(
                                    id,userId,
                                    new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent = new Intent(BarberPageActivity.this, ChatActivity.class);
                                            intent.putExtra(BarberPageActivity.CHAT_ROOM_ID, documentReference.getId());
                                            intent.putExtra(BarberPageActivity.CHAT_ROOM_USER_ID, userId);
                                            intent.putExtra(BarberPageActivity.CHAT_ROOM_BARBER_ID, id);
                                            startActivity(intent);
                                        }
                                    },
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(
                                                    BarberPageActivity.this,
                                                    "Koneksi Gagal",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                            );
                        }
                    }
                });



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