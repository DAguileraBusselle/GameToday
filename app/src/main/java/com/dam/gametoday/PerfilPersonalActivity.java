package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.dialog.AceptarDialog;
import com.dam.gametoday.dialog.OnAceptar;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PerfilPersonalActivity extends AppCompatActivity implements View.OnClickListener, OnAceptar {

    public static final int CLAVE_CAMBIAR_FOTO = 1;
    public static final String CLAVE_CONFIRMAR = "CERRAR SESION";

    TextView btnCerrarSesion, tvNombre, tvCorreo, tvSeguidores, tvSiguiendo, btnPublis, btnLikes, btnMedia;
    View underlinePubli, underlineLike, underlineMedia;
    ImageView ivFotoPerfil, btnCancel;
    RecyclerView rvPublis;

    LinearLayoutManager llm;
    FeedAdapter adapter;

    private ArrayList<Publicacion> listaPublicaciones;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);

        btnPublis = findViewById(R.id.btnPublisPersonal);
        btnLikes = findViewById(R.id.btnLikesPersonal);
        btnMedia = findViewById(R.id.btnMediaPersonal);

        btnPublis.setOnClickListener(this);
        btnLikes.setOnClickListener(this);
        btnMedia.setOnClickListener(this);

        underlinePubli = findViewById(R.id.underlinePublisPersonal);
        underlineLike = findViewById(R.id.underlineLikesPersonal);
        underlineMedia = findViewById(R.id.underlineMediaPersonal);

        listaPublicaciones = new ArrayList<Publicacion>();

        rvPublis = findViewById(R.id.rvPerfilPersonal);

        llm = new LinearLayoutManager(this);
        rvPublis.setLayoutManager(llm);

        adapter = new FeedAdapter(listaPublicaciones, getResources().getIdentifier("@drawable/default_pic", null, getPackageName()));
        rvPublis.setAdapter(adapter);

        tvSiguiendo = findViewById(R.id.tvSiguiendoPersonal);
        tvSeguidores = findViewById(R.id.tvSeguidoresPersonal);

        btnCancel = findViewById(R.id.btnCancelarPerfil);
        btnCancel.setOnClickListener(this);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);

        ivFotoPerfil = findViewById(R.id.ivFotoPerfilPerfil);
        ivFotoPerfil.setOnClickListener(this);

        tvNombre = findViewById(R.id.tvNombreUserPerfil);
        tvCorreo = findViewById(R.id.tvCorreoUserPerfil);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();
        mStorRef = FirebaseStorage.getInstance().getReference();

        underlinePubli.setVisibility(View.VISIBLE);

        bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("userId").getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                        Publicacion publicacion = new Publicacion(snapshot.getKey(),
                                snapshot.child("user").getValue().toString(),
                                snapshot.child("texto").getValue().toString(),
                                (long) snapshot.child("fechaPubliMilis").getValue(),
                                snapshot.child("userId").getValue().toString(),
                                snapshot.child("imagenPubli").getValue().toString(),
                                snapshot.child("likes").getChildrenCount());

                        System.out.println(snapshot.child("fechaPubliMilis").getValue().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy - HH:mm");
                        Date resultDate = new Date(publicacion.getFechaPubli());
                        System.out.println(sdf.format(resultDate));
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

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("seguidores").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tvSeguidores.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
        });

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tvSiguiendo.setText(String.valueOf(dataSnapshot.getChildrenCount() - 1));
            }
        });

        mStorRef.child(mAuth.getCurrentUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivFotoPerfil);
            }
        });

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
            AceptarDialog dialog = new AceptarDialog();
            Bundle args = new Bundle();
            args.putString(CLAVE_CONFIRMAR, getString(R.string.cerrar_sesion_confirm));
            dialog.setArguments(args);
            dialog.setCancelable(true);
            dialog.show(getSupportFragmentManager(), "AceptarDialog");

        } else if (v.equals(ivFotoPerfil)) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, CLAVE_CAMBIAR_FOTO);
        } else if (v.equals(btnCancel)) {
            Intent i = new Intent(PerfilPersonalActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (v.equals(btnPublis)) {
            underlinePubli.setVisibility(View.VISIBLE);
            underlineMedia.setVisibility(View.GONE);
            underlineLike.setVisibility(View.GONE);

            bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaPublicaciones.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("userId").getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
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

        } else if (v.equals(btnLikes)) {
            underlinePubli.setVisibility(View.GONE);
            underlineMedia.setVisibility(View.GONE);
            underlineLike.setVisibility(View.VISIBLE);

            bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaPublicaciones.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for(DataSnapshot snapshotLike : snapshot.child("likes").getChildren()) {
                            System.out.println("snapshotLike.getValue()");
                            System.out.println(snapshotLike.getValue());
                            System.out.println("snapshotLike.getValue()");
                            if (snapshotLike.getValue().equals(mAuth.getCurrentUser().getUid())) {

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

                    }

                    sortListReverse(listaPublicaciones);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (v.equals(btnMedia)) {
            underlinePubli.setVisibility(View.GONE);
            underlineMedia.setVisibility(View.VISIBLE);
            underlineLike.setVisibility(View.GONE);

            bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaPublicaciones.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("userId").getValue().toString().equals(mAuth.getCurrentUser().getUid()) && !snapshot.child("imagenPubli").getValue().equals("no")) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLAVE_CAMBIAR_FOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    Uri imagenUri = data.getData();
                    ivFotoPerfil.setImageURI(imagenUri);
                    subirFoto(imagenUri);
                }
            }
        }
    }

    private void subirFoto(Uri imagenUri) {

        StorageReference fileRef = mStorRef.child(mAuth.getCurrentUser().getUid() + ".jpg");
        fileRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), R.string.toast_foto_subida, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PerfilPersonalActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void aceptar() {
        mAuth.signOut();
        Intent i = new Intent(PerfilPersonalActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}