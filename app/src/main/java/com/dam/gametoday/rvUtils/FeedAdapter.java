package com.dam.gametoday.rvUtils;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.R;
import com.dam.gametoday.model.Publicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedVH> {

    private ArrayList<Publicacion> listaFeed;
    private View.OnClickListener listener;

    public FeedAdapter(ArrayList<Publicacion> feed){this.listaFeed = feed;}

    @NonNull
    @Override
    public FeedAdapter.FeedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_layout, parent, false);
        FeedAdapter.FeedVH vh = new FeedAdapter.FeedVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedVH holder, int position) {
        holder.bindFeed(listaFeed.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFeed.size();}


    public class FeedVH extends RecyclerView.ViewHolder {
        StorageReference mStorRef;

        TextView tvUser, tvTexto, tvHora;
        ImageView ivFoto;

        public FeedVH(@NonNull View itemView) {
            super(itemView);

            ivFoto = itemView.findViewById(R.id.ivProfilePic);

            tvTexto = itemView.findViewById(R.id.tvTextoPubli);
            tvUser = itemView.findViewById(R.id.tvNombreUserPubli);
            tvHora = itemView.findViewById(R.id.tvFechaHoraPubli);
        }

        public void bindFeed(Publicacion publi) {
            tvUser.setText(publi.getUser());
            tvTexto.setText(publi.getTexto());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
            Date resultDate = new Date(publi.getFechaPubli());

            tvHora.setText(sdf.format(resultDate));

            mStorRef = FirebaseStorage.getInstance().getReference();
            System.out.println("##################################################");
            System.out.println(publi.getUser());

            System.out.println(publi.getUserId());
            System.out.println("##################################################");

            mStorRef.child(publi.getUserId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(ivFoto);
                }
            });
        }
    }
}