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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.rvUtils.FeedAdapter;

public class AceptarBorrarPubliDialog extends DialogFragment {


    OnAceptarBorrarPubli listener;
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

        v.setBackground(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/outline_dialog_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));

        btnAceptar.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/tick_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        btnCancel.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/cross_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        tvMensaje.setTextColor(getResources().getColor(((Game2dayApplication) getContext().getApplicationContext()).getTema().getColorChilling()));


        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.trans));

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                tvMensaje.setText(getContext().getString(R.string.borrar_publi_confirm));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });




                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        listener.aceptarBorrarPubli(getArguments().getString(FeedAdapter.CLAVE_BORRAR_PUBLI));
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
        if (context instanceof OnAceptarBorrarPubli) {
            listener = (OnAceptarBorrarPubli) context;
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

