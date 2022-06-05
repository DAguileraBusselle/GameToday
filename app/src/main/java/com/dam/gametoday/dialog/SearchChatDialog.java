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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.gametoday.ChatActivity;
import com.dam.gametoday.Game2dayApplication;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.model.Mensaje;
import com.dam.gametoday.model.Usuario;
import com.dam.gametoday.rvUtils.SearchAdapter;
import com.dam.gametoday.rvUtils.SearchChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchChatDialog extends DialogFragment {

    EditText etTexto;
    ImageView btnCancel, btnBorrarTexto;
    RecyclerView rvUsersChat;
    LinearLayout llEtSearch;


    LinearLayoutManager llm;
    SearchChatAdapter adapter;
    private FirebaseAuth mAuth;


    ArrayList<Usuario> listaUsers = new ArrayList<Usuario>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_abrir_chat, null);
        etTexto = v.findViewById(R.id.etSearchUsuarioChat);
        btnCancel = v.findViewById(R.id.btnCancelarBuscarChat);
        btnBorrarTexto = v.findViewById(R.id.btnBorrarSearchChat);
        rvUsersChat = v.findViewById(R.id.rvUsersChats);
        llEtSearch = v.findViewById(R.id.llSearchUserChat);

        mAuth = FirebaseAuth.getInstance();

        llm = new LinearLayoutManager(getContext());
        adapter = new SearchChatAdapter(listaUsers, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));

        rvUsersChat.setLayoutManager(llm);
        rvUsersChat.setAdapter(adapter);

        btnCancel.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/atras_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        btnBorrarTexto.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/cross_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));

        v.setBackground(ContextCompat.getDrawable(getContext().getApplicationContext(), getContext().getResources().getIdentifier("@drawable/outline_dialog_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getContext().getPackageName())));
        etTexto.setTextColor(getResources().getColor(((Game2dayApplication) getContext().getApplicationContext()).getTema().getColorChilling()));
        llEtSearch.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), getResources().getIdentifier("@drawable/outline_edit_text_" + ((Game2dayApplication) getContext().getApplicationContext()).getColor(), null, getActivity().getPackageName())));


        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.trans));

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {


                btnBorrarTexto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etTexto.setText("");
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
                        if (etTexto.getText().toString().trim().isEmpty()) {
                            listaUsers.clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            buscarUsuario(etTexto.getText().toString().trim());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                adapter.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Usuario user = listaUsers.get(rvUsersChat.getChildAdapterPosition(view));
                        String userId = user.getUserId();
                        i.putExtra(HomeActivity.CLAVE_USUARIO, userId);
                        dialog.dismiss();
                        startActivity(i);
                        getActivity().finish();
                    }
                });


            }
        });

        return ad;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    private void buscarUsuario(String busqueda) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("displayName").startAt(busqueda).endAt(busqueda + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.child("id").getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                        Usuario usuario = new Usuario(snapshot.child("displayName").getValue().toString(), snapshot.child("correo").getValue().toString(), snapshot.getKey());
                        listaUsers.add(usuario);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

}
