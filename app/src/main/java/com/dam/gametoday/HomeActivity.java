package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.fragments.ChatsFragment;
import com.dam.gametoday.fragments.FeedFragment;
import com.dam.gametoday.fragments.SearchFragment;
import com.dam.gametoday.model.Mensaje;
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
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarPubliListener, LifecycleObserver {

    ImageView btnHome, btnSearch, btnChats, btnPerfil;
    TextView tvNumMsjNoLeidos;
    LinearLayout llTvNumMsj;

    public static final String CLAVE_USUARIO = "USUARIO";

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;
    public static Boolean fueraDeCasa = false;

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

        btnChats.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);
        System.out.println(mAuth.getCurrentUser().getUid());
        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").setValue(true);


        FeedFragment feed = new FeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();

        btnHome.setImageDrawable(getResources().getDrawable(R.drawable.homeline));

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
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            btnChats.setImageDrawable(getResources().getDrawable(R.drawable.chatsline));
        }


        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("conectado").onDisconnect().setValue(false);

    }

    private void updateToken() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(mAuth.getCurrentUser().getUid()).setValue(token);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnHome)) {

            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.homeline));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            btnChats.setImageDrawable(getResources().getDrawable(R.drawable.chats));
            fueraDeCasa = false;

        } else if (v.equals(btnSearch)) {

            SearchFragment search = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, search).addToBackStack(null).commit();
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.searchline));
            btnChats.setImageDrawable(getResources().getDrawable(R.drawable.chats));
            fueraDeCasa = true;

        } else if (v.equals(btnChats)) {

            ChatsFragment chats = new ChatsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, chats).addToBackStack(null).commit();
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            btnChats.setImageDrawable(getResources().getDrawable(R.drawable.chatsline));
            fueraDeCasa = true;

        } else if (v.equals(btnPerfil)) {

            Intent i = new Intent (HomeActivity.this, PerfilPersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
            startActivity(i);

        }

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
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.homeline));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            btnChats.setImageDrawable(getResources().getDrawable(R.drawable.chats));
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
                            /*NotificationCompat.Builder builder = new NotificationCompat.Builder(HomeActivity.this, "notifMensaje");
                            bdd.child("Users").child(snapChats.getKey()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    builder.setContentTitle(dataSnapshot.child("displayName").getValue().toString());
                                }
                            });

                            builder.setContentText(snapMensajes.child("texto").getValue().toString());
                            mStorRef.child(snapMensajes.child(snapshot.getKey()) + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onSuccess(Uri uri) {
                                    builder.setSmallIcon(IconCompat.createWithContentUri(uri));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                            builder.setSmallIcon(R.drawable.logosinborde);
                            builder.setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(HomeActivity.this);
                            managerCompat.notify(1, builder.build());


                             */
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

}
