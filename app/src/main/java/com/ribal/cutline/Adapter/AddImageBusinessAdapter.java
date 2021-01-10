package com.ribal.cutline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ribal.cutline.R;
import com.ribal.cutline.model.Barber;
import com.ribal.cutline.model.ImagePage;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddImageBusinessAdapter extends FirestoreRecyclerAdapter<ImagePage, AddImageBusinessAdapter.ImagePageHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AddImageBusinessAdapter(@NonNull FirestoreRecyclerOptions<ImagePage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final AddImageBusinessAdapter.ImagePageHolder holder, int i, @NonNull final ImagePage imagePage) {

        Picasso.get()
                .load(imagePage.getImage())
                .fit()
                .centerCrop()
                .into(holder.imgProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });

    }

    @NonNull
    @Override
    public AddImageBusinessAdapter.ImagePageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new AddImageBusinessAdapter.ImagePageHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }

    class ImagePageHolder extends RecyclerView.ViewHolder {

        Button delete_btn;
        ImageView imgProfile;
        ProgressBar pb;

        public ImagePageHolder(@NonNull final View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.image);

            delete_btn = itemView.findViewById(R.id.delete);
            pb = itemView.findViewById(R.id.progressBar);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    deleteItem(position);

                }
            });

        }
    }


}

