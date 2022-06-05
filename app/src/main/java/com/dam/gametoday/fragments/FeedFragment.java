package com.dam.gametoday.fragments;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.dialog.PublicarDialog;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FeedFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btnPubli;
    RecyclerView rvFeed;

    LinearLayoutManager llm;
    FeedAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    private ArrayList<Publicacion> listaPublicaciones;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        btnPubli = v.findViewById(R.id.btnPublicar);
        btnPubli.setOnClickListener(this);
        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("colorTema").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    switch (dataSnapshot.getValue().toString()) {
                        case "morao":
                            btnPubli.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.morao_chilling)));
                            btnPubli.setRippleColor(getResources().getColor(R.color.morao_chilling));
                            break;
                        case "azul":
                            btnPubli.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.azul_chilling)));
                            btnPubli.setRippleColor(getResources().getColor(R.color.azul_chilling));
                            break;
                        case "verde":
                            btnPubli.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.verde_chilling)));
                            btnPubli.setRippleColor(getResources().getColor(R.color.verde_chilling));
                            break;
                        case "rojo":
                            btnPubli.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rojo_chilling)));
                            btnPubli.setRippleColor(getResources().getColor(R.color.rojo_chilling));
                            break;
                        case "naranja":
                            btnPubli.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja_chilling)));
                            btnPubli.setRippleColor(getResources().getColor(R.color.naranja_chilling));
                            break;

                    }

                }
            }
        });

        rvFeed = v.findViewById(R.id.rvFeed);


        listaPublicaciones = new ArrayList<Publicacion>();

        llm = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(llm);

        adapter = new FeedAdapter(listaPublicaciones, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));
        rvFeed.setAdapter(adapter);

        bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                                if (snapshot.child("userId").getValue().toString().equals(snapshot2.getValue().toString())) {
                                    Publicacion publicacion = new Publicacion(snapshot.getKey(),
                                            snapshot.child("user").getValue().toString(),
                                            snapshot.child("texto").getValue().toString(),
                                            (long) snapshot.child("fechaPubliMilis").getValue(),
                                            snapshot.child("userId").getValue().toString(),
                                            snapshot.child("imagenPubli").getValue().toString(),
                                            snapshot.child("likes").getChildrenCount());

                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
                                    Date resultDate = new Date(publicacion.getFechaPubli());
                                    listaPublicaciones.add(publicacion);
                                }
                            }

                            sortListReverse(listaPublicaciones);


                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }



    @Override
    public void onClick(View v) {
        if (v.equals(btnPubli)) {
            PublicarDialog dialog = new PublicarDialog();
            dialog.setCancelable(true);
            dialog.show(getActivity().getSupportFragmentManager(), "PublicarDialog");

        }
    }

    private void sortListReverse(List<Publicacion> list) {
        Collections.sort(list, new Comparator<Publicacion>() {
            public int compare(Publicacion publi1, Publicacion publi2) {
                // avoiding NullPointerException in case name is null
                Long idea1 = new Long(publi1.getFechaPubli());
                Long idea2 = new Long(publi2.getFechaPubli());
                return idea2.compareTo(idea1);
            }
        });
    }

}

