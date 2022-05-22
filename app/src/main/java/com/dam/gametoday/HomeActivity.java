package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dam.gametoday.dialog.OnAceptarPubliListener;
import com.dam.gametoday.fragments.FeedFragment;
import com.dam.gametoday.fragments.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarPubliListener {

    ImageView btnHome;
    ImageView btnSearch;
    ImageView btnPerfil;

    public static final String CLAVE_USUARIO = "USUARIO";

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;
    private Boolean fueraDeCasa = false;

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
        btnSearch.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);

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

    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnHome)) {

            FeedFragment feed = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, feed).addToBackStack(null).commit();
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.homeline));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            fueraDeCasa = false;


        } if (v.equals(btnSearch)) {

            SearchFragment search = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flHome, search).addToBackStack(null).commit();
            btnHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.lupaline));
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
            fueraDeCasa = false;
        } else {

            finishAffinity();
        }

    }

}
