package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CLAVE_CAMBIAR_FOTO = 1;

    TextView btnCerrarSesion;
    ImageView ivFotoPerfil;
    TextView btnFoto;
    TextView tvNombre;
    TextView tvCorreo;
    ImageView btnCancel;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;
    StorageReference mStorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btnFoto = findViewById(R.id.btnCambiarFoto);
        btnFoto.setOnClickListener(this);

        btnCancel = findViewById(R.id.btnCancelarPerfil);
        btnCancel.setOnClickListener(this);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);

        ivFotoPerfil = findViewById(R.id.ivFotoPerfilPerfil);

        tvNombre = findViewById(R.id.tvNombreUserPerfil);
        tvCorreo = findViewById(R.id.tvCorreoUserPerfil);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();
        mStorRef = FirebaseStorage.getInstance().getReference();

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
            mAuth.signOut();
            Intent i = new Intent(PerfilActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (v.equals(btnFoto)) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, CLAVE_CAMBIAR_FOTO);
        } else if (v.equals(btnCancel)) {
            Intent i = new Intent(PerfilActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PerfilActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}