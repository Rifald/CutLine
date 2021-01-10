package com.ribal.cutline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ribal.cutline.Adapter.OrderHistoryAdapter;
import com.ribal.cutline.Adapter.OrderRequestAdapter;
import com.ribal.cutline.R;
import com.ribal.cutline.activity.OrderDetailActivity;
import com.ribal.cutline.activity.OrderDetailCompleteActivity;
import com.ribal.cutline.activity.ReservationDetailActivity;
import com.ribal.cutline.model.Pesanan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRequestCompleteFragment extends Fragment {
    FirebaseAuth fAuth;
    String userId;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private OrderRequestAdapter adapter;

    public OrderRequestCompleteFragment() {
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
        return inflater.inflate(R.layout.fragment_order_request_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        Query query = fStore.collection("history").whereEqualTo("usahaid",userId).orderBy("sent", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Pesanan> options = new FirestoreRecyclerOptions.Builder<Pesanan>()
                .setQuery(query, Pesanan.class)
                .build();

        adapter = new OrderRequestAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OrderRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();

                Intent i = new Intent(getActivity(), OrderDetailCompleteActivity.class);
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