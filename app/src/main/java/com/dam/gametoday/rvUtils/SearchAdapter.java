package com.dam.gametoday.rvUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> {

    private ArrayList<Usuario> listaSearch;
    private int defaulPic;
    private View.OnClickListener listener;
    Context context;

    public SearchAdapter(ArrayList<Usuario> feed, int defaultPic){this.listaSearch = feed; this.defaulPic = defaultPic;}

    @NonNull
    @Override
    public SearchAdapter.SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_layout, parent, false);
        SearchAdapter.SearchVH vh = new SearchAdapter.SearchVH(v);
        return vh;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchVH holder, int position) {
        holder.bindSearch(listaSearch.get(position));
    }

    @Override
    public int getItemCount() {
        return listaSearch.size();}


    public class SearchVH extends RecyclerView.ViewHolder {
        StorageReference mStorRef;

        private FirebaseAuth mAuth;
        private DatabaseReference bdd;

        TextView tvNombreUser, tvCorreoUser;
        ImageView ivFotoUser;
        Button btnSeguir;

        public SearchVH(@NonNull View itemView) {
            super(itemView);
            tvNombreUser = itemView.findViewById(R.id.tvNombreUserSearch);
            tvCorreoUser = itemView.findViewById(R.id.tvCorreoUserSearch);
            ivFotoUser = itemView.findViewById(R.id.ivFotoPerfilSearch);
            btnSeguir = itemView.findViewById(R.id.btnSeguir);
        }

        public void bindSearch(Usuario user) {

            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            tvNombreUser.setText(user.getDisplayName());
            tvCorreoUser.setText(user.getCorreo());

            tvNombreUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
            tvCorreoUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorTransMenos()));


            mStorRef.child(user.getUserId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(ivFotoUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ivFotoUser.setImageResource(defaulPic);
                }
            });

            bdd.child("Users").child(user.getUserId()).child("seguidores").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean likeDado = false;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                            likeDado = true;
                            btnSeguir.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), context.getResources().getIdentifier("@drawable/outline_button_pressed_" + ((Game2dayApplication) context.getApplicationContext()).getColor(), null, context.getPackageName())));
                            btnSeguir.setTextColor(context.getResources().getColor(R.color.gris_guay));
                            btnSeguir.setText(context.getString(R.string.siguiendo));
                        }
                    }

                    if (!likeDado) {
                        btnSeguir.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), context.getResources().getIdentifier("@drawable/outline_button_" + ((Game2dayApplication) context.getApplicationContext()).getColor(), null, context.getPackageName())));
                        btnSeguir.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
                        btnSeguir.setText(context.getString(R.string.btn_seguir));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            tvCorreoUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PerfilPersonalActivity.class);
                    i.putExtra(HomeActivity.CLAVE_USUARIO, user.getUserId());
                    context.startActivity(i);
                }
            });

            tvNombreUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PerfilPersonalActivity.class);
                    i.putExtra(HomeActivity.CLAVE_USUARIO, user.getUserId());
                    context.startActivity(i);
                }
            });

            ivFotoUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PerfilPersonalActivity.class);
                    i.putExtra(HomeActivity.CLAVE_USUARIO, user.getUserId());
                    context.startActivity(i);
                }
            });

            btnSeguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bdd.child("Users").child(user.getUserId()).child("seguidores").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean siguiendo = false;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                                    siguiendo = true;
                                    bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                                                if (dataSnapshot2.getValue().toString().equals(user.getUserId())) {
                                                    bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").child(dataSnapshot2.getKey()).removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    bdd.child("Users").child(user.getUserId()).child("seguidores").child(dataSnapshot.getKey()).removeValue();
                                    btnSeguir.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), context.getResources().getIdentifier("@drawable/outline_button_pressed_" + ((Game2dayApplication) context.getApplicationContext()).getColor(), null, context.getPackageName())));
                                    btnSeguir.setTextColor(context.getResources().getColor(R.color.gris_guay));
                                    btnSeguir.setText(context.getString(R.string.siguiendo));
                                }
                            }

                            if (!siguiendo) {
                                bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").push().setValue(user.getUserId());
                                bdd.child("Users").child(user.getUserId()).child("seguidores").push().setValue(mAuth.getCurrentUser().getUid());
                                btnSeguir.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), context.getResources().getIdentifier("@drawable/outline_button_" + ((Game2dayApplication) context.getApplicationContext()).getColor(), null, context.getPackageName())));
                                btnSeguir.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
                                btnSeguir.setText(context.getString(R.string.btn_seguir));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    view.setEnabled(false);
                    view.postDelayed(new Runnable(){
                        public void run(){
                            view.setEnabled(true);
                        }
                    }, 175);


                }
            });

        }
    }
}