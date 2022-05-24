package com.dam.gametoday.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.gametoday.ChatActivity;
import com.dam.gametoday.HomeActivity;
import com.dam.gametoday.PerfilPersonalActivity;
import com.dam.gametoday.R;
import com.dam.gametoday.model.Mensaje;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.model.Usuario;
import com.dam.gametoday.rvUtils.ChatsAdapter;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.dam.gametoday.rvUtils.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ChatsFragment extends Fragment {

    RecyclerView rvChats;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;


    ChatsAdapter adapter;
    LinearLayoutManager llm;
    private ArrayList<Mensaje> listaChats = new ArrayList<Mensaje>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        rvChats = v.findViewById(R.id.rvChats);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        llm = new LinearLayoutManager(getContext());
        rvChats.setLayoutManager(llm);

        adapter = new ChatsAdapter(listaChats, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //View rootView = (View) view.getParent();
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Mensaje msj = listaChats.get(rvChats.getChildAdapterPosition(view));
                String user = msj.getParticipante();
                i.putExtra(HomeActivity.CLAVE_USUARIO, user);
                startActivity(i);
                getActivity().finish();
            }
        });

        rvChats.setAdapter(adapter);


        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaChats.clear();
                for (DataSnapshot snapChat : snapshot.getChildren()) {
                    ArrayList<Mensaje> listaMensajesChat = new ArrayList<Mensaje>();
                    for (DataSnapshot snapMensaje : snapChat.getChildren()) {
                        Mensaje msj = new Mensaje(snapChat.getKey(), Boolean.parseBoolean(snapMensaje.child("entrante").getValue().toString()), snapMensaje.child("texto").getValue().toString(), Long.parseLong(snapMensaje.child("fechaMsjMilis").getValue().toString()));
                        listaMensajesChat.add(msj);
                    }
                    sortList(listaMensajesChat);
                    listaChats.add(listaMensajesChat.get(listaMensajesChat.size() - 1));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }


    private void sortList(List<Mensaje> list) {
        Collections.sort(list, new Comparator<Mensaje>() {
            public int compare(Mensaje msj1, Mensaje msj2) {
                // avoiding NullPointerException in case name is null
                Long idea1 = new Long(msj1.getFechaHoraMsj());
                Long idea2 = new Long(msj2.getFechaHoraMsj());
                return idea1.compareTo(idea2);
            }
        });
    }
}