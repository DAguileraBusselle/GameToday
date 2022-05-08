package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener {


    Boolean viendoContra1 = false;

    Button btnAceptar;
    Button btnCancelar;

    EditText etUsuario;
    EditText etContra;
    ImageView btnVerContra;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContrasenia);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnVerContra = findViewById(R.id.btnVerContraInic);

        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

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

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(IniciarSesionActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnCancelar)){
            Intent i = new Intent(IniciarSesionActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else{
            String correo = etUsuario.getText().toString().trim();
            String contra = etContra.getText().toString().trim();

            if (correo.isEmpty()){
                Toast.makeText(this, R.string.toast_intro_correo, Toast.LENGTH_SHORT).show();
            }else {
                if (contra.isEmpty()){
                    Toast.makeText(this, R.string.toast_intro_contra, Toast.LENGTH_SHORT).show();
                }else{
                    iniciarSesion(correo, contra);
                }
            }
        }
    }

    private void iniciarSesion(String correo, String contra) {
        mAuth.signInWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), R.string.toast_exito, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(IniciarSesionActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}