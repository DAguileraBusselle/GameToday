package com.dam.gametoday.fragments;

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

import com.dam.gametoday.R;
import com.dam.gametoday.model.Publicacion;
import com.dam.gametoday.model.Usuario;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.dam.gametoday.rvUtils.SearchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SearchFragment extends Fragment implements View.OnClickListener {

    EditText etSearch;
    RecyclerView rvSearch;
    TextView tvAviso;
    ImageView btnBorrarTexto;

    LinearLayoutManager llm;
    SearchAdapter adapter;

    private ArrayList<Usuario> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        etSearch = v.findViewById(R.id.etSearchUsuario);
        rvSearch = v.findViewById(R.id.rvSearch);
        tvAviso = v.findViewById(R.id.tvAviso);

        btnBorrarTexto = v.findViewById(R.id.btnBorrarSearch);
        btnBorrarTexto.setOnClickListener(this);

        llm = new LinearLayoutManager(getContext());
        rvSearch.setLayoutManager(llm);

        listaUsuarios = new ArrayList<Usuario>();

        adapter = new SearchAdapter(listaUsuarios, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));
        rvSearch.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etSearch.getText().toString().trim().isEmpty()) {
                    tvAviso.setText(getResources().getText(R.string.introduce_search));
                    tvAviso.setVisibility(View.VISIBLE);
                    listaUsuarios.clear();
                    adapter.notifyDataSetChanged();

                } else {
                    tvAviso.setText("");
                    buscarUsuario(etSearch.getText().toString().trim());
                    tvAviso.setVisibility(View.GONE);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return v;
    }

    private void buscarUsuario(String busqueda) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("displayName").startAt(busqueda).endAt(busqueda + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = new Usuario(snapshot.child("displayName").getValue().toString(), snapshot.child("correo").toString(), snapshot.getKey());
                    listaUsuarios.add(usuario);
                }

                if (listaUsuarios.isEmpty()) {
                    tvAviso.setText(getResources().getText(R.string.no_result_search));
                    tvAviso.setVisibility(View.VISIBLE);
                } else {
                    tvAviso.setText("");
                    tvAviso.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnBorrarTexto)) {
            etSearch.setText("");
        }
    }
}