package com.ribal.cutline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ribal.cutline.Adapter.OrderHistoryAdapter;

import com.ribal.cutline.R;
import com.ribal.cutline.fragment.OrderHistoryCompleteFragment;
import com.ribal.cutline.fragment.OrderHistoryFragment;
import com.ribal.cutline.model.Pesanan;

public class OrderHistoryActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    String userId;
    Button inProgress, selesai;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        fragmentProgress();

        selesai = findViewById(R.id.selesai_btn);
        inProgress = findViewById(R.id.inProgrees_btn);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentFinish();
            }
        });

        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentProgress();
            }
        });



    }

    private void fragmentProgress() {
        // Begin the transaction
        Fragment fragment = new OrderHistoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);

        // Complete the changes added above
        fragmentTransaction.commit();
    }
    private void fragmentFinish() {
        // Begin the transaction
        Fragment fragment = new OrderHistoryCompleteFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);

        // Complete the changes added above
        fragmentTransaction.commit();
    }


}