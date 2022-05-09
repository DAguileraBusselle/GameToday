package com.dam.gametoday.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dam.gametoday.R;
import com.dam.gametoday.model.Usuario;
import com.dam.gametoday.rvUtils.FeedAdapter;
import com.dam.gametoday.rvUtils.SearchAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {

    EditText etSearch;
    RecyclerView rvSearch;

    LinearLayoutManager llm;
    SearchAdapter adapter;

    private ArrayList<Usuario> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        etSearch = v.findViewById(R.id.etSearchUsuario);
        rvSearch = v.findViewById(R.id.rvSearch);

        llm = new LinearLayoutManager(getContext());
        rvSearch.setLayoutManager(llm);

        adapter = new SearchAdapter(listaUsuarios, getResources().getIdentifier("@drawable/default_pic", null, getContext().getPackageName()));
        rvSearch.setAdapter(adapter);

        //TODO: AQUI ESTABAS

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return v;
    }

    @Override
    public void onClick(View view) {

    }
}