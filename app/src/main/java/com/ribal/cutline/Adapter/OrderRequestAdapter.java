package com.ribal.cutline.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.ribal.cutline.model.Barber;
import com.ribal.cutline.model.Pesanan;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRequestAdapter extends FirestoreRecyclerAdapter<Pesanan, OrderRequestAdapter.PesananHolder> {

    private OrderRequestAdapter.OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrderRequestAdapter(@NonNull FirestoreRecyclerOptions<Pesanan> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final OrderRequestAdapter.PesananHolder holder, int i, @NonNull final Pesanan pesanan) {

        String userId = pesanan.getUserid();
        holder.statusTv.setText(pesanan.getStatus());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                holder.namaTv.setText(documentSnapshot.getString("nama"));
                holder.alamatTv.setText(documentSnapshot.getString("alamat"));
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users");
        final StorageReference Ref = storageReference.child(userId + "/profile.jpg");
        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                        .fit().centerCrop().into(holder.imgProfile);

            }
        });



    }

    @NonNull
    @Override
    public OrderRequestAdapter.PesananHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false);
        return new OrderRequestAdapter.PesananHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class PesananHolder extends RecyclerView.ViewHolder{

        TextView namaTv, alamatTv, statusTv;
        ImageView imgProfile;


        public PesananHolder(@NonNull View itemView) {
            super(itemView);
            namaTv = itemView.findViewById(R.id.nama_user);
            alamatTv = itemView.findViewById(R.id.alamat_user);
            statusTv = itemView.findViewById(R.id.status_user);
            imgProfile = itemView.findViewById(R.id.profileImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClickListener(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClickListener(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OrderRequestAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


}
