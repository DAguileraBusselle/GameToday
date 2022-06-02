package com.dam.gametoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;

import com.dam.gametoday.model.Mensaje;
import com.dam.gametoday.notif.APIService;
import com.dam.gametoday.notif.Client;
import com.dam.gametoday.rvUtils.MensajesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
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
    ImageView btnEnviar, ivFoto, ivEscribiendo;
    EditText etMensaje;
    TextView tvNombre, tvFecha;

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

    private APIService apiService;

    private ArrayList<Mensaje> listaMensajes = new ArrayList<Mensaje>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setBackgroundDrawableResource(R.drawable.fondohex);

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

        btnEnviar.setOnClickListener(this);
        btnEnviar.setEnabled(false);

        llm = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(llm);

        adapter = new MensajesAdapter(listaMensajes);
        rvMensajes.setAdapter(adapter);

        user = getIntent().getStringExtra(HomeActivity.CLAVE_USUARIO);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

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
                    btnEnviar.setImageDrawable(getResources().getDrawable(R.drawable.sendtrans));
                } else {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setImageDrawable(getResources().getDrawable(R.drawable.send));
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
                        }, 500
                );



            }
        });

        rvMensajes.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int primeraPosicionVisible = llm.findFirstCompletelyVisibleItemPosition();
                int ultimaPosicionVisible = llm.findLastCompletelyVisibleItemPosition();
                if (listaMensajes.get(ultimaPosicionVisible).getFechaHoraMsj() <= System.currentTimeMillis() - 86400000) {
                    if (listaMensajes.get(primeraPosicionVisible).getFechaHoraMsj() <= System.currentTimeMillis() - 86400000) {
                        tvFecha.setVisibility(View.VISIBLE);

                        if (listaMensajes.get(primeraPosicionVisible).getFechaHoraMsj() >= System.currentTimeMillis() - (86400000*2)) {
                            tvFecha.setText(getString(R.string.ayer));
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                            Date resultDate = new Date(listaMensajes.get(primeraPosicionVisible).getFechaHoraMsj());

                            tvFecha.setText(sdf.format(resultDate));
                        }

                    } else  {
                        tvFecha.setVisibility(View.GONE);

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
            String texto = etMensaje.getText().toString();

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


            bdd.child("Tokens").child(user).child("token").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    bdd.child("Users").child(user).child("displayName").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot1) {
                            if (dataSnapshot.getValue() != null) {
                                sendNotifications(dataSnapshot.getValue().toString(), dataSnapshot1.getValue().toString(), texto);

                            }
                        }
                    });

                }
            });

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

    public void sendNotifications(String usertoken, String title, String texto) {
        /*DatosNoti data = new DatosNoti(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    if (response.body().success != 1) {

                        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                        Toast.makeText(ChatActivity.this, "Failed to notify", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

         */


        String registrationToken = usertoken;

// See documentation on defining a message payload.


        RemoteMessage message = new RemoteMessage.Builder(registrationToken)
                .addData("Title", title)
                .addData("Message", texto)
                .build();
        FirebaseMessaging.getInstance().send(message);

// Send a message to the device corresponding to the provided
// registration token.
// Response is a message ID string.
        System.out.println("Successfully sent message: ");
    }

}