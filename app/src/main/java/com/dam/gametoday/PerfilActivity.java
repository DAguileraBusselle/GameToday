package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnCerrarSesion;

    TextView tvNombre;
    TextView tvCorreo;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);

        tvNombre = findViewById(R.id.tvNombreUserPerfil);
        tvCorreo = findViewById(R.id.tvCorreoUserPerfil);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println(Log.e("firebase", "Error getting data", task.getException()));
                }
                else {
                    tvNombre.setText(task.getResult().child("displayName").getValue().toString());
                    tvCorreo.setText(task.getResult().child("correo").getValue().toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnCerrarSesion)) {
            mAuth.signOut();
            Intent i = new Intent(PerfilActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }
}