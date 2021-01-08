package com.ribal.cutline.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.Adapter.OrderHistoryAdapter;
import com.ribal.cutline.R;
import com.ribal.cutline.activity.ChatRoomActivity;
import com.ribal.cutline.activity.FindBarberActivity;
import com.ribal.cutline.activity.OrderHistoryActivity;
import com.ribal.cutline.activity.ReservationDetailActivity;
import com.ribal.cutline.model.Pesanan;
import com.squareup.picasso.Picasso;

public class OrderHistoryCompleteFragment extends Fragment {
    FirebaseAuth fAuth;
    String userId;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private OrderHistoryAdapter adapter;

    public OrderHistoryCompleteFragment() {
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
        return inflater.inflate(R.layout.fragment_order_history_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        Query query = fStore.collection("history").whereEqualTo("userid",userId);
        FirestoreRecyclerOptions<Pesanan> options = new FirestoreRecyclerOptions.Builder<Pesanan>()
                .setQuery(query, Pesanan.class)
                .build();

        adapter = new OrderHistoryAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();

                Intent i = new Intent(getActivity(), ReservationDetailActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}