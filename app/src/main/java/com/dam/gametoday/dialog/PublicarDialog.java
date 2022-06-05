package com.dam.gametoday.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.R;

public class PublicarDialog extends DialogFragment {

    public static final int CLAVE_ELEGIR_FOTO = 2;

    OnAceptarPubliListener listener;
    EditText etTexto;
    ImageView btnCancel, btnAceptar, btnGaleria, ivFotoPreview, btnQuitarImg;

    Uri imagenUri;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_publicar, null);
        etTexto = v.findViewById(R.id.etTextoPubli);
        btnCancel = v.findViewById(R.id.btnCancelarPubli);
        btnAceptar = v.findViewById(R.id.btnAceptarPubli);
        btnGaleria = v.findViewById(R.id.btnSubirImagenPubli);
        btnQuitarImg = v.findViewById(R.id.btnQuitarImagen);

        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(((Game2dayApplication) getContext().getApplicationContext()).getTema().getColorChilling()));
        ViewCompat.setBackgroundTintList(etTexto, colorStateList);

        v.setBackground(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/outline_dialog_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        btnCancel.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/atras_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        btnQuitarImg.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/cross_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        btnGaleria.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/galeria_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));

        etTexto.setTextColor(getResources().getColor(((Game2dayApplication) getContext().getApplicationContext()).getTema().getColorChilling()));
        ivFotoPreview = v.findViewById(R.id.ivPreviewImagenPubli);

        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.trans));

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/ticktrans_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
                btnAceptar.setEnabled(false);

                btnQuitarImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etTexto.getText().toString().trim().isEmpty()) {
                            btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/ticktrans_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));

                            btnAceptar.setEnabled(false);
                        }


                        ivFotoPreview.setImageDrawable(null);
                        imagenUri = null;
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                etTexto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etTexto.getText().toString().trim().isEmpty() && ivFotoPreview.getDrawable() == null) {
                            btnAceptar.setEnabled(false);
                            btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/ticktrans_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
                        } else {
                            btnAceptar.setEnabled(true);
                            btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/tick_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                btnGaleria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(i, CLAVE_ELEGIR_FOTO);
                    }
                });

                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String texto = etTexto.getText().toString().trim();

                        listener.enviarPubli(texto, imagenUri);
                        dialog.dismiss();


                    }
                });
            }
        });

        return ad;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAceptarPubliListener) {
            listener = (OnAceptarPubliListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAceptarDatosListener ");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLAVE_ELEGIR_FOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    btnAceptar.setEnabled(true);
                    btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/tick_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
                    imagenUri = data.getData();
                    ivFotoPreview.setImageURI(imagenUri);
                }
            }
        }
    }


    @Override
    public void onDetach() {
        if (listener != null) {
            listener = null;
        }
        super.onDetach();
    }

}
