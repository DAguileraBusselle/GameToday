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
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.model.Usuario;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchChatAdapter extends RecyclerView.Adapter<SearchChatAdapter.ChatsVH>
        implements View.OnClickListener {

    private ArrayList<Usuario> listaUsers;
    private int defaulPic;
    private View.OnClickListener listener;
    Context context;

    public SearchChatAdapter(ArrayList<Usuario> feed, int defaultPic){this.listaUsers = feed; this.defaulPic = defaultPic;}

    @NonNull
    @Override
    public SearchChatAdapter.ChatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_user_layout, parent, false);
        SearchChatAdapter.ChatsVH vh = new SearchChatAdapter.ChatsVH(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchChatAdapter.ChatsVH holder, int position) {
        holder.bindChats(listaUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return listaUsers.size();}

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

        TextView tvNombreUser, tvCorreoUser;
        ImageView ivFotoUser;

        public ChatsVH(@NonNull View itemView) {
            super(itemView);
            tvNombreUser = itemView.findViewById(R.id.tvNombreUserSearchChat);
            tvCorreoUser = itemView.findViewById(R.id.tvCorreoUserSearchChat);
            ivFotoUser = itemView.findViewById(R.id.ivFotoPerfilSearchChat);
        }

        public void bindChats(Usuario user) {

            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            tvNombreUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorChilling()));
            tvCorreoUser.setTextColor(context.getResources().getColor(((Game2dayApplication) context.getApplicationContext()).getTema().getColorTransMenos()));


            tvNombreUser.setText(user.getDisplayName());
            tvCorreoUser.setText(user.getCorreo());

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

        }
    }
}
