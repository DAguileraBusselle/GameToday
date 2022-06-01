package com.dam.gametoday.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditarPubliDialog extends DialogFragment {

    public static final int CLAVE_ELEGIR_FOTO = 2;

    OnEditarPubliListener listener;
    EditText etTexto;
    ImageView btnCancel, btnAceptar, btnGaleria, ivFotoPreview, btnQuitarImg;

    Uri imagenUri;

    StorageReference mStorRef;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

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

        ivFotoPreview = v.findViewById(R.id.ivPreviewImagenPubli);

        mStorRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.trans));

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                btnAceptar.setImageDrawable(getResources().getDrawable(R.drawable.tick_trans));

                btnAceptar.setEnabled(false);

                btnQuitarImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etTexto.getText().toString().trim().isEmpty()) {
                            btnAceptar.setImageDrawable(getResources().getDrawable(R.drawable.tick_trans));

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

                bdd.child("Publicaciones").child(getArguments().getString(FeedAdapter.CLAVE_EDITAR_PUBLI)).child("texto").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        etTexto.setText(dataSnapshot.getValue().toString());
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
                            btnAceptar.setImageDrawable(getResources().getDrawable(R.drawable.tick_trans));
                        } else {
                            btnAceptar.setEnabled(true);
                            btnAceptar.setImageDrawable(getResources().getDrawable(R.drawable.tick));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                bdd.child("Publicaciones").child(getArguments().getString(FeedAdapter.CLAVE_EDITAR_PUBLI)).child("imagenPubli").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {


                        mStorRef.child(dataSnapshot.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ivFotoPreview.setVisibility(View.GONE);
                                Picasso.get().cancelRequest(ivFotoPreview);

                                if(!dataSnapshot.getValue().toString().equals("no")) {
                                    ivFotoPreview.setVisibility(View.VISIBLE);
                                    Picasso.get().load(uri).into(ivFotoPreview);
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ivFotoPreview.setImageResource(0);
                                ivFotoPreview.setVisibility(View.GONE);


                            }
                        });
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

                        listener.aceptarEditarPubli(texto, imagenUri, getArguments().getString(FeedAdapter.CLAVE_EDITAR_PUBLI));
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
        if (context instanceof OnEditarPubliListener) {
            listener = (OnEditarPubliListener) context;
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
                    btnAceptar.setImageDrawable(getResources().getDrawable(R.drawable.tick));
                    imagenUri = data.getData();
                    ivFotoPreview.setVisibility(View.VISIBLE);
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
