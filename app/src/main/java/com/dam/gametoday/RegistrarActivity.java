package com.dam.gametoday;

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


        btnRegistrar.setEnabled(false);

        btnVerContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = etContra.getSelectionStart();
                int end = etContra.getSelectionEnd();

                viendoContra1 = !viendoContra1;
                if (viendoContra1) {
                    btnVerContra.setImageResource(getResources().getIdentifier("@drawable/ojolineamorado", null, getPackageName()));
                    etContra.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    btnVerContra.setImageResource(getResources().getIdentifier("@drawable/ojoabiertomorado", null, getPackageName()));
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
                    btnVerContraCheck.setImageResource(getResources().getIdentifier("@drawable/ojolineamorado", null, getPackageName()));
                    etContraCheck.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    btnVerContraCheck.setImageResource(getResources().getIdentifier("@drawable/ojoabiertomorado", null, getPackageName()));
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
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.rojo_suave));
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
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            String nombre = etNombre.getText().toString().trim();
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
                    Toast.makeText(this, "HASTA AQUI BIEN", Toast.LENGTH_SHORT).show();
                    //TODO: registro en base de datos
                }
            }
        }
    }
}