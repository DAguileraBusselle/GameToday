package com.dam.gametoday.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.R;
import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.dialog.PublicarDialog;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

        btnPubli = v.findViewById(R.id.btnPublicar);
        btnPubli.setOnClickListener(this);

        rvFeed = v.findViewById(R.id.rvFeed);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        listaPublicaciones = new ArrayList<Publicacion>();

        llm = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(llm);

        adapter = new FeedAdapter(listaPublicaciones, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));
        rvFeed.setAdapter(adapter);

        actualizarFeed();


        return v;
    }

    private void actualizarFeed() {

        FirebaseDatabase.getInstance().getReference().child("Publicaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Publicacion publicacion = new Publicacion(snapshot.getKey(), snapshot.child("user").getValue().toString(), snapshot.child("texto").getValue().toString(), (long) snapshot.child("fechaPubliMilis").getValue(), snapshot.child("userId").getValue().toString());
                    System.out.println(snapshot.child("fechaPubliMilis").getValue().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
                    Date resultDate = new Date(publicacion.getFechaPubli());
                    System.out.println(sdf.format(resultDate));
                    listaPublicaciones.add(publicacion);
                }

                sortListReverse(listaPublicaciones);


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
