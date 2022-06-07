package com.dam.gametoday.rvUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.model.Mensaje;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsVH>
        implements View.OnClickListener {

    private ArrayList<Mensaje> listaChats;
    private int defaulPic;
    private View.OnClickListener listener;
    Context context;

    public ChatsAdapter(ArrayList<Mensaje> feed, int defaultPic){this.listaChats = feed; this.defaulPic = defaultPic;}

    @NonNull
    @Override
    public ChatsAdapter.ChatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chats_layout, parent, false);
        ChatsAdapter.ChatsVH vh = new ChatsAdapter.ChatsVH(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatsVH holder, int position) {
        holder.bindChats(listaChats.get(position));
    }

    @Override
    public int getItemCount() {
        return listaChats.size();}

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public class ChatsVH extends RecyclerView.ViewHolder {
        StorageReference mStorRef;

        private FirebaseAuth mAuth;
        private DatabaseReference bdd;


        TextView tvNombreUser, tvFechaHora, tvUltimoMsj, tvCorreoUser, tvNumMensajes;
        ImageView ivFotoUser, ivCheck, ivEstado;
        LinearLayout llNumMensajes;

        public ChatsVH(@NonNull View itemView) {
            super(itemView);
            tvNombreUser = itemView.findViewById(R.id.tvNombreUserChats);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHoraChats);
            tvUltimoMsj = itemView.findViewById(R.id.tvTextoChats);
            tvCorreoUser = itemView.findViewById(R.id.tvCorreoUserChats);
            ivFotoUser = itemView.findViewById(R.id.ivFotoPerfilChats);
            ivCheck = itemView.findViewById(R.id.ivCheckChats);
            ivEstado = itemView.findViewById(R.id.ivConectado);

            tvNumMensajes = itemView.findViewById(R.id.tvNumMsjNoLeidosChat);
            llNumMensajes = itemView.findViewById(R.id.llNumMensajesChat);
        }

        public void bindChats(Mensaje mensaje) {

            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            tvNombreUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
            tvCorreoUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorTransMenos()));
            tvFechaHora.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorTransMenos()));

            llNumMensajes.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), context.getResources().getIdentifier("@drawable/num_mensajes_no_leidos_" + ((Game2dayApplication) context.getApplicationContext()).getColor(), null, context.getPackageName())));


            bdd.child("Users").child(mensaje.getParticipante()).child("displayName").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    tvNombreUser.setText(dataSnapshot.getValue().toString());
                }
            });

            bdd.child("Users").child(mensaje.getParticipante()).child("correo").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    tvCorreoUser.setText(dataSnapshot.getValue().toString());
                }
            });

            tvUltimoMsj.setText(mensaje.getTexto());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
            Date resultDate = new Date(mensaje.getFechaHoraMsj());

            tvFechaHora.setText(sdf.format(resultDate));

            if (mensaje.getMsjEntrante()) {
                tvUltimoMsj.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));

                ivCheck.setVisibility(View.GONE);
            } else {
                ivCheck.setVisibility(View.VISIBLE);
                if (mensaje.getLeido().equals("no")) {
                    ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.enviado));
                } else {
                    ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.visto));
                }
                tvUltimoMsj.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorrChilling()));
            }

            mStorRef.child(mensaje.getParticipante() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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


            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(mensaje.getParticipante()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cont = 0;
                for (DataSnapshot snapChats : snapshot.getChildren()) {
                    if (!snapChats.getKey().equals("escribiendo")) {
                        if (snapChats.child("entrante").getValue().equals(true) && snapChats.child("leido").getValue().equals("no")) {
                            cont ++;
                        }
                    }

                }
                if (cont > 0) {
                    llNumMensajes.setVisibility(View.VISIBLE);
                    tvNumMensajes.setText(String.valueOf(cont));
                    itemView.setBackground(context.getDrawable(((Game2dayApplication) context.getApplicationContext()).getTema().getColorTransMenos()));
                } else {
                    llNumMensajes.setVisibility(View.GONE);
                    itemView.setBackground(context.getDrawable(R.color.gris_guay_medio_trans_menos));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            bdd.child("Users").child(mensaje.getParticipante()).child("chats").child(mAuth.getCurrentUser().getUid()).child("escribiendo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue().equals(true)) {
                            tvUltimoMsj.setText(context.getString(R.string.escribiendo));
                            tvFechaHora.setText("");
                            ivCheck.setVisibility(View.GONE);
                        } else {
                            tvUltimoMsj.setText(mensaje.getTexto());

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");
                            Date resultDate = new Date(mensaje.getFechaHoraMsj());

                            tvFechaHora.setText(sdf.format(resultDate));

                            if (mensaje.getMsjEntrante()) {
                                tvUltimoMsj.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
                                ivCheck.setVisibility(View.GONE);
                            } else {
                                ivCheck.setVisibility(View.VISIBLE);
                                if (mensaje.getLeido().equals("no")) {
                                    ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.enviado));
                                } else {
                                    ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.visto));
                                }
                                tvUltimoMsj.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorrChilling()));
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            bdd.child("Users").child(mensaje.getParticipante()).child("conectado").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue().equals(true)) {
                            ivEstado.setImageDrawable(context.getResources().getDrawable(R.drawable.conectado));
                        } else {
                            ivEstado.setImageDrawable(context.getResources().getDrawable(R.drawable.desconectado));
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
