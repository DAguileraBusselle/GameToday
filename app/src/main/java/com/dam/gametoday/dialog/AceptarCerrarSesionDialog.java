package com.dam.gametoday.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;

public class AceptarCerrarSesionDialog extends DialogFragment {


    OnAceptarCerrarSesion listener;
    ImageView btnCancel;
    ImageView btnAceptar;
    TextView tvMensaje;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_aceptar, null);
        btnCancel = v.findViewById(R.id.btnCancelarConfirm);
        btnAceptar = v.findViewById(R.id.btnAceptarConfirm);
        tvMensaje = v.findViewById(R.id.tvTextoConfirmacion);

        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.trans));

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                tvMensaje.setText(getContext().getString(R.string.cerrar_sesion_confirm));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });




                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        listener.aceptarCerrarSesion();
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
        if (context instanceof OnAceptarCerrarSesion) {
            listener = (OnAceptarCerrarSesion) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAceptar ");
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

