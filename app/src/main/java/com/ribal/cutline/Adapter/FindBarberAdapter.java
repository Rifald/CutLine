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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ribal.cutline.R;
import com.ribal.cutline.model.Barber;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FindBarberAdapter extends  FirestoreRecyclerAdapter<Barber, FindBarberAdapter.BarberHolder>{

    private FindBarberAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FindBarberAdapter(@NonNull FirestoreRecyclerOptions<Barber> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final FindBarberAdapter.BarberHolder holder, int i, @NonNull final Barber barber) {

        holder.namaTv.setText(barber.getNama());
        holder.alamatTv.setText(barber.getAlamat());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Barber");
        final StorageReference Ref = storageReference.child(barber.getNama()  + "/Profile.jpg");
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
    public FindBarberAdapter.BarberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barber,parent,false);
        return new FindBarberAdapter.BarberHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class BarberHolder extends RecyclerView.ViewHolder{

        TextView namaTv, alamatTv;
        ImageView imgProfile;

        public BarberHolder(@NonNull View itemView) {
            super(itemView);
            namaTv = itemView.findViewById(R.id.nama_usaha);
            alamatTv = itemView.findViewById(R.id.alamat_usaha);
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

    public void setOnClickListener(FindBarberAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


}
