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
import com.dam.gametoday.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> {

    private ArrayList<Usuario> listaSearch;
    private int defaulPic;
    private View.OnClickListener listener;

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

        TextView tvNombreUser;
        ImageView ivFotoUser;

        public SearchVH(@NonNull View itemView) {
            super(itemView);
            tvNombreUser = itemView.findViewById(R.id.tvNombreUserSearch);
            ivFotoUser = itemView.findViewById(R.id.ivFotoPerfilSearch);
        }

        public void bindSearch(Usuario user) {

            mStorRef = FirebaseStorage.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            bdd = FirebaseDatabase.getInstance().getReference();

            tvNombreUser.setText(user.getDisplayName());

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