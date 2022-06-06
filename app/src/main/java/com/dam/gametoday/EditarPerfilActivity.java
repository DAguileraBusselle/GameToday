package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Locale;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CLAVE_CAMBIAR_FOTO_EDIT_PERFIL = 1;

    TextView tvContBio, tv250, tvNombre, tvBio, tvFoto;
    EditText etBio, etNombre;
    Button btnAceptar, btnCancelar;
    ImageView ivFotoPerfil;
    LinearLayout btnCambiarFoto;
    View underlineFoto;

    boolean fotoCambiada = false;
    Uri imagenUri;
    StorageReference mStorRef;
    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mStorRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        tvContBio = findViewById(R.id.tvContadorBio);
        tv250 = findViewById(R.id.tv250);
        tvFoto = findViewById(R.id.tvCambiarFoto);
        tvNombre = findViewById(R.id.tvTextoNombreUserEditarPerfil);
        tvBio = findViewById(R.id.tvTextoBioEditarPerfil);
        underlineFoto = findViewById(R.id.underlineCambiarFoto);
        etBio = findViewById(R.id.etCambiarBio);
        etNombre = findViewById(R.id.etCambiarUsername);
        btnAceptar = findViewById(R.id.btnAceptarEditarPerfil);
        btnCancelar = findViewById(R.id.btnCancelarEditarPerfil);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfilEditarPerfil);
        btnCambiarFoto = findViewById(R.id.btnCambiarFotoPerfil);

        btnCambiarFoto.setOnClickListener(this);
        ivFotoPerfil.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        tvFoto.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        tv250.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        tvContBio.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        tvNombre.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorrChillingTrans()));
        tvBio.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorrChillingTrans()));

        underlineFoto.setBackground(getResources().getDrawable(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));

        etBio.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorTransMenos()));
        etNombre.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorTransMenos()));
        etNombre.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling())));
        etBio.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling())));

        btnAceptar.setBackground(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/outline_button_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));
        btnAceptar.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        btnCancelar.setBackground(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/outline_button_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));
        btnCancelar.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));



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
                    etNombre.setText(task.getResult().child("displayName").getValue().toString());
                }
            }
        });

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("bio").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    etBio.setText(dataSnapshot.getValue().toString());
                    tvContBio.setText(String.valueOf(dataSnapshot.getValue().toString().length()));
                } else {
                    tvContBio.setText("0");
                }

            }
        });

        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textoNombre = etNombre.getText().toString().trim();
                if (textoNombre.isEmpty()) {
                    btnAceptar.setEnabled(false);
                } else {
                    btnAceptar.setEnabled(true);
                }

            }
        });

        etBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textoBio = etBio.getText().toString().trim();
                if (textoBio.length() > 0) {
                    tvContBio.setText(String.valueOf(textoBio.length()));

                } else  {
                    tvContBio.setText("0");

                }

                if (textoBio.length() > 250) {
                    btnAceptar.setEnabled(false);
                    tvContBio.setTextColor(getResources().getColor(R.color.red));

                } else {
                    btnAceptar.setEnabled(true);
                    tvContBio.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnCancelar)) {
            finish();
        } else if (view.equals(btnCambiarFoto) || view.equals(ivFotoPerfil)) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, CLAVE_CAMBIAR_FOTO_EDIT_PERFIL);
        } else if (view.equals(btnAceptar)) {
            if (fotoCambiada) {
                subirFoto(imagenUri);
            }

            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("displayName").setValue(etNombre.getText().toString().trim().toLowerCase());

            if (!etBio.getText().toString().trim().isEmpty()) {
                bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("bio").setValue(etBio.getText().toString().trim());

            }

            Intent i = new Intent (EditarPerfilActivity.this, PerfilPersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(HomeActivity.CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
            startActivity(i);
            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLAVE_CAMBIAR_FOTO_EDIT_PERFIL) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    imagenUri = data.getData();
                    ivFotoPerfil.setImageURI(imagenUri);
                    fotoCambiada = true;
                }
            }
        }
    }
}