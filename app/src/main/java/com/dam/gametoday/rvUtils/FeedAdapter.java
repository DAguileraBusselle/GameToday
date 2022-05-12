package com.dam.gametoday.rvUtils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.R;
import com.dam.gametoday.fragments.FeedFragment;
import com.dam.gametoday.model.Publicacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedVH> {

    private ArrayList<Publicacion> listaFeed;
    private int defaulPic;
    private View.OnClickListener listener;
    Context context;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
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

        TextView tvUser, tvTexto, tvHora, tvNumLikes;
        ImageView ivFoto, btnBorrar, btnLike, ivImagenPubli;

        public FeedVH(@NonNull View itemView) {
            super(itemView);

            ivFoto = itemView.findViewById(R.id.ivProfilePic);
            ivImagenPubli = itemView.findViewById(R.id.ivImagenPubli);

            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            tvTexto = itemView.findViewById(R.id.tvTextoPubli);
            tvUser = itemView.findViewById(R.id.tvNombreUserPubli);
            tvHora = itemView.findViewById(R.id.tvFechaHoraPubli);

            btnLike = itemView.findViewById(R.id.btnLikePubli);
            btnBorrar = itemView.findViewById(R.id.btnBorrarPubli);
        }

        public void bindFeed(Publicacion publi) {


            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            System.out.println("##################################################");
            System.out.println(publi.getUser());

            System.out.println(publi.getUserId());
            System.out.println("##################################################");

            ivFoto.setImageResource(defaulPic);

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

            ivImagenPubli.setVisibility(View.GONE);

            if(!publi.getImagenPubli().equals("no")) {
                mStorRef.child(publi.getImagenPubli()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ivImagenPubli.setVisibility(View.VISIBLE);


                        Picasso.get().load(uri).into(ivImagenPubli);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Picasso.get().cancelRequest(ivImagenPubli);

                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        System.out.println(publi.getTexto() + "NO SE PUDO CARGAR LA IMAGEN: " + e.getMessage());
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                    }
                });
            } else {
                Picasso.get().cancelRequest(ivImagenPubli);
            }

            tvUser.setText(publi.getUser());
            tvTexto.setText(publi.getTexto());

            tvNumLikes.setText(String.valueOf(publi.getLikes()));

            bdd.child("Publicaciones").child(publi.getPubliId()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean likeDado = false;


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                            likeDado = true;
                            btnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_full));
                        }
                    }

                    if (!likeDado) {
                        btnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bdd.child("Publicaciones").child(publi.getPubliId()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean likeDado = false;


                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                                    likeDado = true;
                                    bdd.child("Publicaciones").child(publi.getPubliId()).child("likes").child(dataSnapshot.getKey()).removeValue();
                                    btnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
                                    tvNumLikes.setText(String.valueOf(Integer.parseInt(tvNumLikes.getText().toString()) - 1));
                                }
                            }

                            if (!likeDado) {
                                bdd.child("Publicaciones").child(publi.getPubliId()).child("likes").push().setValue(mAuth.getCurrentUser().getUid());
                                btnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_full));
                                tvNumLikes.setText(String.valueOf(Integer.parseInt(tvNumLikes.getText().toString()) + 1));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
            Date resultDate = new Date(publi.getFechaPubli());

            tvHora.setText(sdf.format(resultDate));


            if (publi.getUserId().equals(mAuth.getCurrentUser().getUid())) {
                btnBorrar.setVisibility(View.VISIBLE);
                btnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mStorRef.child(publi.getImagenPubli()).delete();
                        bdd.child("Publicaciones").child(publi.getPubliId()).removeValue();

                        FeedFragment feed = new FeedFragment();
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();

                    }
                });

            } else {
                btnBorrar.setVisibility(View.INVISIBLE);
            }

        }
    }
}