package com.dam.gametoday.rvUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajeVH> {

    private ArrayList<Mensaje> listaMensajes;
    Context context;

    public MensajesAdapter(ArrayList<Mensaje> feed){this.listaMensajes = feed;}

    @NonNull
    @Override
    public MensajesAdapter.MensajeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mensaje_layout, parent, false);
        MensajesAdapter.MensajeVH vh = new MensajesAdapter.MensajeVH(v);
        return vh;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesAdapter.MensajeVH holder, int position) {
        holder.bindMensaje(listaMensajes.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();}
    
    public class MensajeVH extends RecyclerView.ViewHolder {

        TextView tvTextoEnt, tvFechaHoraEnt;
        LinearLayout llMensajeEnt;
        TextView tvTextoSal, tvFechaHoraSal;
        LinearLayout llMensajeSal;

        ImageView ivVisto;

        public MensajeVH(@NonNull View itemView) {
            super(itemView);
        }

        public void bindMensaje(Mensaje mensaje) {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date resultDate = new Date(mensaje.getFechaHoraMsj());

            llMensajeEnt = itemView.findViewById(R.id.llMensajeEntrante);
            tvTextoEnt = itemView.findViewById(R.id.tvTextoMensajeEntr);
            tvFechaHoraEnt = itemView.findViewById(R.id.tvFechaHoraMensajeEntr);
            llMensajeSal = itemView.findViewById(R.id.llMensajeSaliente);
            tvTextoSal = itemView.findViewById(R.id.tvTextoMensajeSal);
            tvFechaHoraSal = itemView.findViewById(R.id.tvFechaHoraMensajeSal);

            ivVisto = itemView.findViewById(R.id.ivCheckMensaje);





            if (mensaje.getMsjEntrante()) {
                llMensajeEnt.setVisibility(View.VISIBLE);
                llMensajeSal.setVisibility(View.GONE);
                tvTextoEnt.setText(mensaje.getTexto());
                tvFechaHoraEnt.setText(sdf.format(resultDate));

            } else {
                llMensajeSal.setVisibility(View.VISIBLE);
                llMensajeEnt.setVisibility(View.GONE);
                tvTextoSal.setText(mensaje.getTexto());
                tvFechaHoraSal.setText(sdf.format(resultDate));

                if (mensaje.getLeido().equals("no")) {
                    ivVisto.setImageDrawable(context.getResources().getDrawable(R.drawable.enviado));
                } else {
                    ivVisto.setImageDrawable(context.getResources().getDrawable(R.drawable.visto));
                }

            }



        }
    }
}
