package com.dam.gametoday.rvUtils;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.R;
import com.dam.gametoday.model.Publicacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedVH> {

    private ArrayList<Publicacion> listaFeed;
    private int defaulPic;
    private View.OnClickListener listener;

    public FeedAdapter(ArrayList<Publicacion> feed, int defaultPic){this.listaFeed = feed; this.defaulPic = defaultPic;}

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

        private FirebaseAuth mAuth;
        private DatabaseReference bdd;

        TextView tvUser, tvTexto, tvHora;
        ImageView ivFoto, btnBorrar;

        public FeedVH(@NonNull View itemView) {
            super(itemView);

            ivFoto = itemView.findViewById(R.id.ivProfilePic);

            tvTexto = itemView.findViewById(R.id.tvTextoPubli);
            tvUser = itemView.findViewById(R.id.tvNombreUserPubli);
            tvHora = itemView.findViewById(R.id.tvFechaHoraPubli);

            btnBorrar = itemView.findViewById(R.id.btnBorrarPubli);
        }

        public void bindFeed(Publicacion publi) {
            tvUser.setText(publi.getUser());
            tvTexto.setText(publi.getTexto());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
            Date resultDate = new Date(publi.getFechaPubli());

            tvHora.setText(sdf.format(resultDate));

            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            System.out.println("##################################################");
            System.out.println(publi.getUser());

            System.out.println(publi.getUserId());
            System.out.println("##################################################");

            mStorRef.child(publi.getUserId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(ivFoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ivFoto.setImageResource(defaulPic);
                }
            });

            if (publi.getUserId().equals(mAuth.getCurrentUser().getUid())) {
                btnBorrar.setVisibility(View.VISIBLE);
                btnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        bdd.child("Publicaciones").child(publi.getPubliId()).removeValue();
                    }
                });

            } else {
                btnBorrar.setVisibility(View.INVISIBLE);
            }

        }
    }
}