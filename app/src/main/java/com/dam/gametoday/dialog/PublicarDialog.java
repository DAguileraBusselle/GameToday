package com.dam.gametoday.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class PublicarDialog extends DialogFragment {

    OnAceptarPubliListener listener;
    EditText etTexto;
    ImageView btnCancel;
    ImageView btnAceptar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_publicar, null);
        etTexto = v.findViewById(R.id.etTextoPubli);
        btnCancel = v.findViewById(R.id.btnCancelarPubli);
        btnAceptar = v.findViewById(R.id.btnAceptarPubli);

        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String texto = etTexto.getText().toString().trim();

                        if (texto.isEmpty()) {

                            //TODO: disable button hasta que haya escrito algo
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_debe_escribir, Toast.LENGTH_SHORT).show();

                        } else {
                            listener.enviarPubli(texto);
                            dialog.dismiss();
                        }



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
    public void onDetach() {
        if (listener != null) {
            listener = null;
        }
        super.onDetach();
    }

}
