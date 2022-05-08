package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.fragments.FeedFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarPubliListener {

    ImageView btnHome;
    ImageView btnSearch;
    ImageView btnPerfil;


    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        btnHome = findViewById(R.id.btnHome);
        btnSearch = findViewById(R.id.btnSearch);
        btnPerfil = findViewById(R.id.btnPersonalProfile);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        btnHome.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);

        FeedFragment feed = new FeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();

    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnHome)) {

            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();

        } else if (v.equals(btnPerfil)) {
            Intent i = new Intent (HomeActivity.this, PerfilActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
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
                    map.put("fechaPubliMilis", System.currentTimeMillis() + 7200000);
                    ref.child(idPubli).setValue(map);
                }
            }
        });
    }
}
