package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dam.gametoday.model.Mensaje;
import com.dam.gametoday.rvUtils.MensajesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvMensajes;
    ImageView btnEnviar, ivFoto, ivEscribiendo, btnSalir;
    EditText etMensaje;
    TextView tvNombre, tvFecha;
    LinearLayout llMandar;

    StorageReference mStorRef;
    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    String user;

    private Timer timer = new Timer();
    MensajesAdapter adapter;
    LinearLayoutManager llm;
    DatabaseReference bddRef;
    DatabaseReference bddRef2;
    ValueEventListener listener;
    ValueEventListener listener2;

    private ArrayList<Mensaje> listaMensajes = new ArrayList<Mensaje>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/fondo_" + ((Game2dayApplication) getApplicationContext()).getFondo(), null, getPackageName())));

        getWindow().setNavigationBarColor(getResources().getColor(R.color.trans_menos));

        mStorRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        rvMensajes = findViewById(R.id.rvMensajes);
        etMensaje = findViewById(R.id.etEscribirMensaje);
        btnEnviar = findViewById(R.id.btnMandarMsj);
        ivFoto = findViewById(R.id.ivFotoPerfilMensajes);
        tvNombre = findViewById(R.id.tvNombreUserMensajes);
        tvFecha = findViewById(R.id.tvFechaMensajes);
        ivEscribiendo = findViewById(R.id.ivEscribiendo);
        llMandar = findViewById(R.id.llMandarMensaje);
        btnSalir = findViewById(R.id.btnCancelarChat);


        llMandar.setBackground(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/outline_edit_text_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));
        tvNombre.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        etMensaje.setTextColor(getResources().getColor(((Game2dayApplication) getApplicationContext()).getTema().getColorChilling()));
        btnEnviar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/sendtrans_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));
        btnSalir.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/atras_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));

        btnSalir.setOnClickListener(this);
        btnEnviar.setOnClickListener(this);
        btnEnviar.setEnabled(false);

        llm = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(llm);

        adapter = new MensajesAdapter(listaMensajes);
        rvMensajes.setAdapter(adapter);

        user = getIntent().getStringExtra(HomeActivity.CLAVE_USUARIO);

        bdd.child("Users").child(user).child("displayName").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tvNombre.setText(dataSnapshot.getValue().toString());
            }
        });

        bddRef2 = bdd.child("Users").child(user).child("chats").child(mAuth.getCurrentUser().getUid()).child("escribiendo");

        listener2 = bddRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().equals(true)) {
                        ivEscribiendo.setVisibility(View.VISIBLE);
                    } else {
                        ivEscribiendo.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvMensajes.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int bottom, int i4, int i5, int i6, int oldBottom) {
                if ( bottom < oldBottom) {
                    rvMensajes.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvMensajes.scrollToPosition(rvMensajes.getAdapter().getItemCount() - 1);
                        }
                    }, 0);
                }

            }
        });


        mStorRef.child(user + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivFoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ivFoto.setImageDrawable(getResources().getDrawable(R.drawable.default_pic));
            }
        });

        bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user).child("escribiendo").setValue(false);


        etMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (etMensaje.getText().toString().trim().isEmpty()) {
                    btnEnviar.setEnabled(false);
                    btnEnviar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/sendtrans_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));

                } else {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getResources().getIdentifier("@drawable/send_" + ((Game2dayApplication) getApplicationContext()).getColor(), null, getPackageName())));
                }

                if (!etMensaje.getText().toString().trim().isEmpty()) {
                    bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user).child("escribiendo").setValue(true);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user).child("escribiendo").setValue(false);

                            }
                        }, 850
                );



            }
        });

        rvMensajes.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int primeraPosicionVisible = llm.findFirstVisibleItemPosition();
                int ultimaPosicionVisible = llm.findLastVisibleItemPosition();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>");

                System.out.println(listaMensajes.get(ultimaPosicionVisible).getTexto());
                System.out.println(listaMensajes.get(primeraPosicionVisible).getTexto());
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>");


                if (listaMensajes.get(primeraPosicionVisible).getFechaHoraMsj() <= System.currentTimeMillis() - 86400000) {
                    tvFecha.setVisibility(View.VISIBLE);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
                    Date resultDate2 = new Date(listaMensajes.get(primeraPosicionVisible).getFechaHoraMsj());

                    String fechaMensaje = sdf2.format(resultDate2);

                    SimpleDateFormat sdfAyer = new SimpleDateFormat("dd/MM/yy");
                    Date resultDateAyer = new Date(System.currentTimeMillis() - 86400000);

                    String fechaAyer = sdfAyer.format(resultDateAyer);

                    if (fechaMensaje.equals(fechaAyer)) {
                        tvFecha.setText(getString(R.string.ayer));
                    } else {
                        tvFecha.setText(fechaMensaje);

                    }

                    } else  {
                   tvFecha.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChatActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bddRef != null && listener != null && listener2 != null) {
            bddRef.removeEventListener(listener);
            bddRef2.removeEventListener(listener2);
        }
        startActivity(i);
        HomeActivity.fueraDeCasa = true;
        finish();
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

    @Override
    public void onClick(View view) {
        if (view.equals(btnEnviar)) {
            String texto = etMensaje.getText().toString().trim();

            HashMap<String, Object> msj = new HashMap<>();
            msj.put("entrante", false);
            msj.put("texto", texto);
            msj.put("leido", "no");
            msj.put("fechaMsjMilis", System.currentTimeMillis());

            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user).push().setValue(msj);

            HashMap<String, Object> msj2 = new HashMap<>();
            msj2.put("entrante", true);
            msj2.put("texto", texto);
            msj2.put("leido", "no");
            msj2.put("fechaMsjMilis", System.currentTimeMillis());

            bdd.child("Users").child(user).child("chats").child(mAuth.getCurrentUser().getUid()).push().setValue(msj2);

            etMensaje.setText("");

        }else if (view.equals((btnSalir))){
            Intent i = new Intent(ChatActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (bddRef != null && listener != null && listener2 != null) {
                bddRef.removeEventListener(listener);
                bddRef2.removeEventListener(listener2);
            }
            startActivity(i);
            HomeActivity.fueraDeCasa = true;
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        bddRef = bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user);

        listener = bddRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fechaUltimoMsj = "";
                listaMensajes.clear();
                for (DataSnapshot snapMensaje : snapshot.getChildren()) {
                    if (!snapMensaje.getKey().equals("escribiendo")) {
                        Boolean primerMsjDelDia = false;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        Date resultDate = new Date(Long.parseLong(snapMensaje.child("fechaMsjMilis").getValue().toString()));
                        System.out.println(sdf.format(resultDate) + " || " + snapMensaje.child("texto").getValue().toString());
                        if (!fechaUltimoMsj.equals(sdf.format(resultDate))) {
                            primerMsjDelDia = true;
                        }

                        fechaUltimoMsj = sdf.format(resultDate);

                        if (snapMensaje.child("entrante").getValue().equals(true)) {
                            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("chats").child(user).child(snapMensaje.getKey()).child("leido").setValue("si");
                            bdd.child("Users").child(user).child("chats").child(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapChatOtroUser : dataSnapshot.getChildren()) {
                                        if (!snapChatOtroUser.getKey().equals("escribiendo")) {
                                            if (snapChatOtroUser.child("entrante").getValue().equals(false)) {
                                                bdd.child("Users").child(user).child("chats").child(mAuth.getCurrentUser().getUid()).child(snapChatOtroUser.getKey()).child("leido").setValue("si");
                                            }
                                        }
                                    }
                                }
                            });
                        }

                        Mensaje msj = new Mensaje(user,
                                Boolean.parseBoolean(snapMensaje.child("entrante").getValue().toString()),
                                snapMensaje.child("texto").getValue().toString(),
                                snapMensaje.child("leido").getValue().toString(),
                                Long.parseLong(snapMensaje.child("fechaMsjMilis").getValue().toString()),
                                primerMsjDelDia);
                        listaMensajes.add(msj);
                    }
                }

                sortList(listaMensajes);

                adapter.notifyDataSetChanged();
                rvMensajes.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}