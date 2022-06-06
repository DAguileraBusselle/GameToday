package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class RegistrarActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNombre;
    EditText etCorreo;
    EditText etContra;
    EditText etContraCheck;

    Button btnRegistrar;
    Button btnCancelar;

    ImageView btnVerContra;
    ImageView btnVerContraCheck;

    Boolean viendoContra1 = false;
    Boolean viendoContra2 = false;

    Intent i;

    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        etNombre = findViewById(R.id.etUsuarioRegistar);
        etCorreo = findViewById(R.id.etCorreo);
        etContra = findViewById(R.id.etContraseniaRegistrar);
        etContraCheck = findViewById(R.id.etContraseniaCheck);


        btnCancelar = findViewById(R.id.btnCancelarRegistar);
        btnRegistrar = findViewById(R.id.btnRegistrarRegistrar);

        btnRegistrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        btnVerContra = findViewById(R.id.btnVerContra);
        btnVerContraCheck = findViewById(R.id.btnVerContraCheck);

        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        btnRegistrar.setEnabled(false);

        btnVerContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = etContra.getSelectionStart();
                int end = etContra.getSelectionEnd();

                viendoContra1 = !viendoContra1;
                if (viendoContra1) {
                    btnVerContra.setImageResource(getResources().getIdentifier("@drawable/ojoabiertomorado", null, getPackageName()));
                    etContra.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    btnVerContra.setImageResource(getResources().getIdentifier("@drawable/ojolineamorado", null, getPackageName()));
                    etContra.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                etContra.setSelection(start, end);

            }
        });

        btnVerContraCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = etContraCheck.getSelectionStart();
                int end = etContraCheck.getSelectionEnd();

                viendoContra2 = !viendoContra2;
                if (viendoContra2) {
                    btnVerContraCheck.setImageResource(getResources().getIdentifier("@drawable/ojoabiertomorado", null, getPackageName()));
                    etContraCheck.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    btnVerContraCheck.setImageResource(getResources().getIdentifier("@drawable/ojolineamorado", null, getPackageName()));
                    etContraCheck.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                etContraCheck.setSelection(start, end);

            }
        });


        etContraCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etContraCheck.getText().toString().equals(etContra.getText().toString())) {
                    btnRegistrar.setEnabled(true);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.morao_chilling));
                    ViewCompat.setBackgroundTintList(etContraCheck, colorStateList);
                } else {
                    btnRegistrar.setEnabled(false);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
                    ViewCompat.setBackgroundTintList(etContraCheck, colorStateList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etContra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etContraCheck.getText().toString().equals(etContra.getText().toString())) {
                    btnRegistrar.setEnabled(true);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.morao_chilling));
                    ViewCompat.setBackgroundTintList(etContraCheck, colorStateList);
                } else {
                    btnRegistrar.setEnabled(false);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.rojo_suave));
                    ViewCompat.setBackgroundTintList(etContraCheck, colorStateList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnCancelar)) {
            Intent i = new Intent(RegistrarActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            String nombre = etNombre.getText().toString().trim().toLowerCase();
            String correo = etCorreo.getText().toString().trim();
            String contra = etContra.getText().toString().trim();
            String contraCheck = etContraCheck.getText().toString().trim();

            if(nombre.isEmpty() || correo.isEmpty() ||
                    contra.isEmpty() || contraCheck.isEmpty()) {
                Toast.makeText(this, R.string.toast_vacio, Toast.LENGTH_SHORT).show();

            } else {
                if (!contraCheck.equals(contra)) {
                    Toast.makeText(this, R.string.toast_contra_check_fail, Toast.LENGTH_SHORT).show();

                } else {
                    if (contraCheck.length() < 6) {
                        Toast.makeText(this, R.string.toast_contra_corta, Toast.LENGTH_SHORT).show();

                    } else {
                        registrarUsuario(nombre, correo, contra);
                    }

                }
            }
        }
    }

    private void registrarUsuario(String displayName, String correo, String contra) {

        auth.createUserWithEmailAndPassword(correo, contra).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("displayName", displayName);
                map.put("correo", correo);
                map.put("id", auth.getCurrentUser().getUid());
                map.put("colorTema", "morao");
                map.put("fondo", "hex");

                rootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            rootRef.child("Users").child(auth.getCurrentUser().getUid()).child("siguiendo").push().setValue(auth.getCurrentUser().getUid());
                            Toast.makeText(getApplicationContext(), R.string.toast_usuario_creado, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent (RegistrarActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.toast_usuario_no_creado + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}