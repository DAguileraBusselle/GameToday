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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

        //bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("p2jaA3REYERK73xz88iy2ZqDngm2");
        //bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("fem22GVFPFUV2bxgcsiYiYbGDRz2");
/*

        HashMap<String, Object> msj1 = new HashMap<>();
        msj1.put("entrante", true);
        msj1.put("texto", "hola marta que tal");
        msj1.put("fechaMsjMilis", System.currentTimeMillis());

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("p2jaA3REYERK73xz88iy2ZqDngm2").push().setValue(msj1);

        HashMap<String, Object> msj2 = new HashMap<>();
        msj2.put("entrante", false);
        msj2.put("texto", "the fitness grand pacer test is a multicapacity aerobics capacity test");
        msj2.put("fechaMsjMilis", System.currentTimeMillis() + 1000);

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("p2jaA3REYERK73xz88iy2ZqDngm2").push().setValue(msj2);

        HashMap<String, Object> msj3 = new HashMap<>();
        msj3.put("entrante", true);
        msj3.put("texto", "fuite a la verga malparido");
        msj3.put("fechaMsjMilis", System.currentTimeMillis() + 2000);

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("p2jaA3REYERK73xz88iy2ZqDngm2").push().setValue(msj3);


 */ /*
        HashMap<String, Object> msj4 = new HashMap<>();
        msj4.put("entrante", true);
        msj4.put("texto", "UWU");
        msj4.put("fechaMsjMilis", System.currentTimeMillis() - 1000);

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child("fem22GVFPFUV2bxgcsiYiYbGDRz2").push().setValue(msj4);
        */


        if (mAuth.getCurrentUser() != null) {
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