package com.dam.gametoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvContBio;
    EditText etBio;
    Button btnAceptar, btnCancelar;

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
        etBio = findViewById(R.id.etCambiarBio);
        btnAceptar = findViewById(R.id.btnAceptarEditarPerfil);
        btnCancelar = findViewById(R.id.btnCancelarEditarPerfil);

        btnCancelar.setOnClickListener(this);

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("bio").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    etBio.setText(dataSnapshot.getValue().toString());
                    tvContBio.setText(dataSnapshot.getValue().toString().length());
                } else {
                    tvContBio.setText("0");
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
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
                    ViewCompat.setBackgroundTintList(etBio, colorStateList);
                    btnAceptar.setEnabled(false);
                    tvContBio.setTextColor(getResources().getColor(R.color.red));

                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.morao_chilling));
                    ViewCompat.setBackgroundTintList(etBio, colorStateList);
                    btnAceptar.setEnabled(true);
                    tvContBio.setTextColor(getResources().getColor(R.color.morao_trans_menos));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnCancelar)) {
            finish();
        }
    }
}