package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.dialog.OnAceptarBorrarPubli;
import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.dialog.OnEditarPubliListener;
import com.dam.gametoday.fragments.ChatsFragment;
import com.dam.gametoday.fragments.FeedFragment;
import com.dam.gametoday.fragments.SearchFragment;
import com.dam.gametoday.model.Tema;
import com.dam.gametoday.model.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarPubliListener, LifecycleObserver, OnAceptarBorrarPubli, OnEditarPubliListener {

    ImageView btnHome, btnSearch, btnChats, btnPerfil;
    TextView tvNumMsjNoLeidos;
    LinearLayout llTvNumMsj;

    public static final String CLAVE_USUARIO = "USUARIO";

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;
    public static Boolean fueraDeCasa = false;
    public static Boolean fotoEditada = false;

    Drawable imagenHomeLinea;
    Drawable imagenHome;
    Drawable imagenSearchLinea;
    Drawable imagenSearch;
    Drawable imagenChatsLinea;
    Drawable imagenChats;

    DatabaseReference bddRef;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        tvNumMsjNoLeidos = findViewById(R.id.tvNumMsjNoLeidosHome);
        llTvNumMsj = findViewById(R.id.llNumMensajes);
        btnChats = findViewById(R.id.btnChats);
        btnHome = findViewById(R.id.btnHome);
        btnSearch = findViewById(R.id.btnSearch);
        btnPerfil = findViewById(R.id.btnPersonalProfile);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("colorTema").setValue("verde");

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("colorTema").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    establecerTema(dataSnapshot.getValue().toString());
                    ((Game2dayApplication) getApplicationContext()).setColor(dataSnapshot.getValue().toString());


                } else {
                    establecerTema("");
                    ((Game2dayApplication) getApplicationContext()).setColor("morao");
                }
                establecerIconosYColores();

            }
        });



        btnChats.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);
        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").setValue(true);



        FeedFragment feed = new FeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();


        mStorRef = FirebaseStorage.getInstance().getReference();

        mStorRef.child(mAuth.getCurrentUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(btnPerfil);
            }
        });

        updateToken();

        if (fueraDeCasa) {
            ChatsFragment chats = new ChatsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, chats).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHome);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChatsLinea);
        }


        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").onDisconnect().setValue(false);


    }

    private void establecerIconosYColores() {
        imagenHomeLinea = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/homeline_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));
        imagenHome = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/home_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));
        imagenSearchLinea = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/searchline_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));
        imagenSearch = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/search_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));
        imagenChatsLinea = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/chatsline_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));
        imagenChats = ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/chats_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName()));

        llTvNumMsj.setBackground(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/num_mensajes_no_leidos_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));

        if (fueraDeCasa) {
            ChatsFragment chats = new ChatsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, chats).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHome);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChatsLinea);
        } else {
            btnHome.setImageDrawable(imagenHomeLinea);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChats);

        }


    }

    private void establecerTema(String color) {
        int colorTransOscuro = R.color.morao_trans_oscuro;
        int colorTransMenos = R.color.morao_trans_menos;
        int colorTransMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos;
        int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
        int colorrChilling = R.color.morrao_chilling;
        int colorrChillingTrans = R.color.morrao_chilling_trans;
        int colorChilling = R.color.morao_chilling;

        switch (color) {
            case "verde":
                colorTransOscuro = R.color.verde_trans_oscuro;
                colorTransMenos = R.color.verde_trans_menos;
                colorTransMenosMasMenosMasMasMenos = R.color.verde_trans_menos_mas_menos_mas_mas_menos;
                colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.verde_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                colorrChilling = R.color.verrde_chilling;
                colorrChillingTrans = R.color.verrde_chilling_trans;
                colorChilling = R.color.verde_chilling;
                break;
            case "rojo":
                colorTransOscuro = R.color.rojo_trans_oscuro;
                colorTransMenos = R.color.rojo_trans_menos;
                colorTransMenosMasMenosMasMasMenos = R.color.rojo_trans_menos_mas_menos_mas_mas_menos;
                colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.rojo_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                colorrChilling = R.color.rrojo_chilling;
                colorrChillingTrans = R.color.rrojo_chilling_trans;
                colorChilling = R.color.rojo_chilling;
                break;
            case "azul":
                colorTransOscuro = R.color.azul_trans_oscuro;
                colorTransMenos = R.color.azul_trans_menos;
                colorTransMenosMasMenosMasMasMenos = R.color.azul_trans_menos_mas_menos_mas_mas_menos;
                colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.azul_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                colorrChilling = R.color.azzul_chilling;
                colorrChillingTrans = R.color.azzul_chilling_trans;
                colorChilling = R.color.azul_chilling;
                break;
            case "naranja":
                colorTransOscuro = R.color.naranja_trans_oscuro;
                colorTransMenos = R.color.naranja_trans_menos;
                colorTransMenosMasMenosMasMasMenos = R.color.naranja_trans_menos_mas_menos_mas_mas_menos;
                colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.naranja_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                colorrChilling = R.color.narranja_chilling;
                colorrChillingTrans = R.color.narranja_chilling_trans;
                colorChilling = R.color.naranja_chilling;
                break;

        }

        Tema tema = new Tema(colorTransOscuro, colorTransMenos, colorTransMenosMasMenosMasMasMenos,
                colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos, colorrChilling,
                colorrChillingTrans, colorChilling);

        ((Game2dayApplication) getApplicationContext()).setTema(tema);


    }

    private void updateToken() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(firebaseUser.getUid()).setValue(token);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnHome)) {

            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHomeLinea);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChats);
            fueraDeCasa = false;

        } else if (v.equals(btnSearch)) {

            SearchFragment search = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, search).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHome);
            btnSearch.setImageDrawable(imagenSearchLinea);
            btnChats.setImageDrawable(imagenChats);
            fueraDeCasa = true;

        } else if (v.equals(btnChats)) {

            ChatsFragment chats = new ChatsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, chats).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHome);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChatsLinea);
            fueraDeCasa = true;

        } else if (v.equals(btnPerfil)) {

            Intent i = new Intent (HomeActivity.this, PerfilPersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
            startActivity(i);

        }

    }

    @Override
    public void aceptarEditarPubli(String texto, Uri imagen, String id) {
        bdd.child("Publicaciones").child(id).child("texto").setValue(texto);
        if (imagen == null) {
            bdd.child("Publicaciones").child(id).child("imagenPubli").setValue("no");

            System.out.println(id + "_publi.jpg");

            mStorRef.child(id + "_publi.jpg").delete();
            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
        } else {
            bdd.child("Publicaciones").child(id).child("imagenPubli").setValue(id + "_publi.jpg");


            StorageReference fileRef = mStorRef.child(id + "_publi.jpg");
            fileRef.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    FeedFragment feed = new FeedFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        fotoEditada = false;
    }

    @Override
    public void enviarPubli(String texto, Uri imagen) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Publicaciones");
        String idPubli = ref.push().getKey();

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("displayName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println(Log.e("firebase", "Error getting data", task.getException()));
                }
                else {
                    if(imagen == null) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("user", task.getResult().getValue());
                        map.put("texto", texto);
                        map.put("userId", mAuth.getCurrentUser().getUid());
                        map.put("fechaPubliMilis", System.currentTimeMillis());
                        map.put("imagenPubli", "no");
                        ref.child(idPubli).setValue(map);

                        FeedFragment feed = new FeedFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("user", task.getResult().getValue());
                        map.put("texto", texto);
                        map.put("userId", mAuth.getCurrentUser().getUid());
                        map.put("fechaPubliMilis", System.currentTimeMillis());
                        map.put("imagenPubli", idPubli + "_publi.jpg");
                        ref.child(idPubli).setValue(map);

                        StorageReference fileRef = mStorRef.child(idPubli + "_publi.jpg");
                        fileRef.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), R.string.toast_foto_subida, Toast.LENGTH_SHORT).show();

                                FeedFragment feed = new FeedFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (fueraDeCasa) {
            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
            btnHome.setImageDrawable(imagenHomeLinea);
            btnSearch.setImageDrawable(imagenSearch);
            btnChats.setImageDrawable(imagenChats);
            fueraDeCasa = false;
        } else {
            if (bddRef != null && listener != null) {
                bddRef.removeEventListener(listener);
            }
            finishAffinity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        bddRef = bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats");

        listener = bddRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cont = 0;
                for (DataSnapshot snapChats : snapshot.getChildren()) {
                    for (DataSnapshot snapMensajes : snapChats.getChildren()) {
                        if (!snapMensajes.getKey().equals("escribiendo")) {

                            if (snapMensajes.child("entrante").getValue().equals(true) && snapMensajes.child("leido").getValue().equals("no")) {
                                cont++;

                            }
                        }
                    }
                }
                if (cont > 0) {
                    llTvNumMsj.setVisibility(View.VISIBLE);
                    tvNumMsjNoLeidos.setText(String.valueOf(cont));
                } else {
                    llTvNumMsj.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        if (mAuth != null) {
            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").setValue(false);

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if (mAuth != null) {

            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").setValue(true);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed() {
        if (mAuth != null) {
            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").setValue(false);

        }

    }

    @Override
    public void aceptarBorrarPubli(String idPubli) {
        mStorRef.child(idPubli + "_publi.jgp").delete();
        bdd.child("Publicaciones").child(idPubli).removeValue();

        FeedFragment feed = new FeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
    }


}
