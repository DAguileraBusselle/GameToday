package com.dam.gametoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnRegistrar;
    Button btnIniciar;

    Intent i;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciar = findViewById(R.id.btnIniciarSesion);
        btnIniciar.setOnClickListener(this);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        //Ejecutar al menos una vez el sign out para crear un usuario nuevo
        //mAuth.signOut();

        if (mAuth.getCurrentUser() != null && bdd.child("Users").child(mAuth.getCurrentUser().getUid()) != null) {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnRegistrar)) {
            i = new Intent(this, RegistrarActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            i = new Intent(this, IniciarSesionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}