package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.dialog.PublicarDialog;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.model.Usuario;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarPubliListener {

    FloatingActionButton btnPubli;
    RecyclerView rvFeed;

    LinearLayoutManager llm;
    FeedAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPubli = findViewById(R.id.btnPublicar);
        btnPubli.setOnClickListener(this);

        rvFeed = findViewById(R.id.rvFeed);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        ArrayList<Publicacion> listaPublicaciones = new ArrayList<Publicacion>();

        Query mQuery = bdd.child("Publicaciones");

        ValueEventListener feedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Publicacion publi;
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    publi = new Publicacion(String.valueOf(next.child("user").getValue()), String.valueOf(next.child("texto").getValue()));
                    listaPublicaciones.add(publi);

                }

                llm = new LinearLayoutManager(getApplicationContext());
                rvFeed.setLayoutManager(llm);

                adapter = new FeedAdapter(listaPublicaciones);
                rvFeed.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HOME ACTIVITY", "loadPost:onCancelled");
            }
        };

        mQuery.addListenerForSingleValueEvent(feedListener);

        /*
        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("displayName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println(Log.e("firebase", "Error getting data", task.getException()));
                }
                else {

                }
            }
        });

        ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();

        Query mQuery = bdd.child("Users").orderByKey();

        ValueEventListener mQueryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user;
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    user = new Usuario(String.valueOf(next.child("displayName").getValue()), String.valueOf(next.child("correo").getValue()), String.valueOf(next.child("id").getValue()));
                    listaUsuarios.add(user);
                    System.out.println(next.child("displayName") + " || " + user.getDisplayName());

                }

                String texto = "";

                for (Usuario us : listaUsuarios) {
                    System.out.println(us.getDisplayName());
                    texto += us.getUserId() + " - " + us.getDisplayName() + " - " + us.getCorreo() + "\n" ;
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mQuery.addListenerForSingleValueEvent(mQueryValueListener);

        */

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnPubli)) {

            PublicarDialog dialog = new PublicarDialog();
            dialog.setCancelable(false);
            dialog.show(this.getSupportFragmentManager(), "PublicarDialog");

        }
    }

    @Override
    public void enviarPubli(String texto) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Publicaciones");
        String idPubli = ref.push().getKey();

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("displayName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println(Log.e("firebase", "Error getting data", task.getException()));
                }
                else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("user", task.getResult().getValue());
                    map.put("texto", texto);
                    map.put("userId", mAuth.getCurrentUser().getUid());
                    ref.child(idPubli).setValue(map);
                }
            }
        });




    }
}
