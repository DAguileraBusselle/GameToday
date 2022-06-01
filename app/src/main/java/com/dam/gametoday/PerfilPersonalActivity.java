package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.dialog.AceptarCerrarSesionDialog;
import com.dam.gametoday.dialog.OnAceptarBorrarPubli;
import com.dam.gametoday.dialog.OnAceptarCerrarSesion;
import com.dam.gametoday.fragments.FeedFragment;
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

public class PerfilPersonalActivity extends AppCompatActivity implements View.OnClickListener, OnAceptarCerrarSesion, OnAceptarBorrarPubli {

    public static final int CLAVE_CAMBIAR_FOTO = 1;


    TextView tvNombre, tvCorreo, tvSeguidores, tvSiguiendo, btnPublis, btnLikes, btnMedia;
    View underlinePubli, underlineLike, underlineMedia;
    ImageView ivFotoPerfil, btnCancel, btnMsj, btnMenu;
    RecyclerView rvPublis;
    Button btnSeguir, btnEditar;

    LinearLayoutManager llm;
    FeedAdapter adapter;

    private ArrayList<Publicacion> listaPublicaciones;

    private String user;
    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();
        mStorRef = FirebaseStorage.getInstance().getReference();


        ivFotoPerfil = findViewById(R.id.ivFotoPerfilPerfil);
        btnEditar = findViewById(R.id.btnEditarPrefil);

        btnMenu = findViewById(R.id.imgMenu);
        btnMenu.setOnClickListener(this);

        if(getIntent().getStringExtra(HomeActivity.CLAVE_USUARIO).equals(mAuth.getCurrentUser().getUid())){
            user = mAuth.getCurrentUser().getUid();
            ivFotoPerfil.setOnClickListener(this);
            btnEditar.setVisibility(View.VISIBLE);
            btnEditar.setEnabled(true);
            //TODO: editar perfil
        }else{
            user = getIntent().getStringExtra(HomeActivity.CLAVE_USUARIO);

            btnMsj = findViewById(R.id.btnMandarMsj);
            btnMsj.setVisibility(View.VISIBLE);
            btnMsj.setOnClickListener(this);

            btnSeguir = findViewById(R.id.btnSeguirPerfil);
            btnSeguir.setVisibility(View.VISIBLE);
            btnSeguir.setEnabled(true);
            btnSeguir.setOnClickListener(this);
            bdd.child("Users").child(user).child("seguidores").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean siguiendo = false;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                            siguiendo = true;
                            btnSeguir.setBackground(getResources().getDrawable(R.drawable.outline_button_pressed));
                            btnSeguir.setTextColor(getResources().getColor(R.color.gris_guay));
                            btnSeguir.setText(getString(R.string.siguiendo));
                        }
                    }

                    if (!siguiendo) {
                        btnSeguir.setBackground(getResources().getDrawable(R.drawable.outline_button));
                        btnSeguir.setTextColor(getResources().getColor(R.color.morao_chilling));
                        btnSeguir.setText(getString(R.string.btn_seguir));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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


        tvNombre = findViewById(R.id.tvNombreUserPerfil);
        tvCorreo = findViewById(R.id.tvCorreoUserPerfil);


        underlinePubli.setVisibility(View.VISIBLE);

        bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("userId").getValue().toString().equals(user)) {
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

        bdd.child("Users").child(user).child("seguidores").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tvSeguidores.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
        });

        bdd.child("Users").child(user).child("siguiendo").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tvSiguiendo.setText(String.valueOf(dataSnapshot.getChildrenCount() - 1));
            }
        });

        mStorRef.child(user + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivFotoPerfil);
            }
        });

        bdd.child("Users").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
        if(v.equals(btnMenu)) {
            Context wrapper = new ContextThemeWrapper(PerfilPersonalActivity.this,  R.style.AppTheme_PopupMenu);
            PopupMenu menu = new PopupMenu(wrapper, btnMenu);
            menu.getMenuInflater().inflate(R.menu.menu_perfil, menu.getMenu());

            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menuSettings) {
                        Intent i = new Intent(PerfilPersonalActivity.this, SettingsActivity.class);
                        startActivity(i);
                    } else if (item.getItemId() == R.id.menuSignOut){
                        AceptarCerrarSesionDialog dialog = new AceptarCerrarSesionDialog();
                        dialog.setCancelable(true);
                        dialog.show(getSupportFragmentManager(), "AceptarDialog");
                    }

                    return true;
                }
            });

            menu.show();

        } else if (v.equals(ivFotoPerfil)) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, CLAVE_CAMBIAR_FOTO);
        } else if (v.equals(btnMsj)) {
            Intent i = new Intent(PerfilPersonalActivity.this, ChatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(HomeActivity.CLAVE_USUARIO, user);
            startActivity(i);
            finish();
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

                        if (snapshot.child("userId").getValue().toString().equals(user)) {
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
                            if (snapshotLike.getValue().equals(user)) {

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

                        if (snapshot.child("userId").getValue().toString().equals(user) && !snapshot.child("imagenPubli").getValue().equals("no")) {
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

        } else if (v.equals(btnSeguir)) {
            bdd.child("Users").child(user).child("seguidores").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean siguiendo = false;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                            siguiendo = true;
                            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                                        if (dataSnapshot2.getValue().toString().equals(user)) {
                                            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").child(dataSnapshot2.getKey()).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            bdd.child("Users").child(user).child("seguidores").child(dataSnapshot.getKey()).removeValue();
                            btnSeguir.setBackground(getResources().getDrawable(R.drawable.outline_button));
                            btnSeguir.setTextColor(getResources().getColor(R.color.morao_chilling));
                            btnSeguir.setText(getString(R.string.btn_seguir));
                        }
                    }

                    if (!siguiendo) {
                        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("siguiendo").push().setValue(user);
                        bdd.child("Users").child(user).child("seguidores").push().setValue(mAuth.getCurrentUser().getUid());
                        btnSeguir.setBackground(getResources().getDrawable(R.drawable.outline_button_pressed));
                        btnSeguir.setTextColor(getResources().getColor(R.color.gris_guay));
                        btnSeguir.setText(getString(R.string.siguiendo));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnSeguir.setEnabled(false);
            btnSeguir.postDelayed(new Runnable(){
                public void run(){
                    btnSeguir.setEnabled(true);
                }
            }, 175);



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

        StorageReference fileRef = mStorRef.child(user + ".jpg");
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
    public void aceptarCerrarSesion() {
        mAuth.signOut();
        Intent i = new Intent(PerfilPersonalActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void aceptarBorrarPubli(String idPubli) {

        mStorRef.child(idPubli + "_publi.jgp").delete();
        bdd.child("Publicaciones").child(idPubli).removeValue();

        underlinePubli.setVisibility(View.VISIBLE);
        underlineMedia.setVisibility(View.GONE);
        underlineLike.setVisibility(View.GONE);

        bdd.child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPublicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("userId").getValue().toString().equals(user)) {
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