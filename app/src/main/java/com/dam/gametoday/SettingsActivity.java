package com.dam.gametoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gametoday.model.Tema;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    View underlineTemaMorao, underlineTemaRojo, underlineTemaVerde, underlineTemaAzul, underlineTemaNaranja;

    View underlineFondoHex, underlineFondoPkm, underlineFondoSunset, underlineFondoDoom,
            underlineFondoOrange, underlineFondoSw, underlineFondoCircuito, underlineFondoGameboy,
            underlineFondoCalle, underlineFondoMandos, underlineFondoMetal, underlineFondoMarioAmarillo,
            underlineFondoMarioAzul, underlineFondoMarioNaranja, underlineFondoMarioBlanco;

    LinearLayout llTemas, btnTemas, llFondo, btnFondos;

    LinearLayout btnTemaAzul, btnTemaMorao, btnTemaRojo, btnTemaVerde, btnTemaNaranja;

    LinearLayout btnFondoHex, btnFondoPkm, btnFondoSunset, btnFondoDoom,
            btnFondoOrange, btnFondoSw, btnFondoCircuito, btnFondoGameboy,
            btnFondoCalle, btnFondoMandos, btnFondoMetal, btnFondoMarioAmarillo,
            btnFondoMarioAzul, btnFondoMarioNaranja, btnFondoMarioBlanco;

    ImageView itemTemas;
    ImageView itemFondos;

    TextView btnCancelarTemas, btnAplicarTemas;
    TextView btnCancelarFondos, btnAplicarFondos;

    ImageView btnSalir;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    boolean temasVisibles = true;
    boolean fondosVisibles = true;
    String temaElegido;
    String fondoElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        temasVisibles = false;
        fondosVisibles = false;

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();

        btnSalir = findViewById(R.id.btnCancelarAjustes);
        btnSalir.setOnClickListener(this);

        itemTemas = findViewById(R.id.itemTemasAjustes);
        itemFondos = findViewById(R.id.itemFondosAjustes);
        underlineTemaAzul = findViewById(R.id.underlineTemaAzul);
        underlineTemaRojo = findViewById(R.id.underlineTemaRojo);
        underlineTemaVerde = findViewById(R.id.underlineTemaVerde);
        underlineTemaMorao = findViewById(R.id.underlineTemaMorao);
        underlineTemaNaranja = findViewById(R.id.underlineTemaNaranja);

        underlineFondoCircuito = findViewById(R.id.underlineFondoCircuito);
        underlineFondoDoom = findViewById(R.id.underlineFondoDoom);
        underlineFondoHex = findViewById(R.id.underlineFondoHex);
        underlineFondoPkm = findViewById(R.id.underlineFondoPkm);
        underlineFondoOrange = findViewById(R.id.underlineFondoOrange);
        underlineFondoSunset = findViewById(R.id.underlineFondoSunset);
        underlineFondoSw = findViewById(R.id.underlineFondoSw);
        underlineFondoGameboy = findViewById(R.id.underlineFondoGameboy);
        underlineFondoCalle = findViewById(R.id.underlineFondoCalle);
        underlineFondoMandos = findViewById(R.id.underlineFondoMandos);
        underlineFondoMetal = findViewById(R.id.underlineFondoMetal);
        underlineFondoMarioAmarillo = findViewById(R.id.underlineFondoMarioAmarillo);
        underlineFondoMarioAzul = findViewById(R.id.underlineFondoMarioAzul);
        underlineFondoMarioNaranja = findViewById(R.id.underlineFondoMarioNaranja);
        underlineFondoMarioBlanco = findViewById(R.id.underlineFondoMarioBlanco);

        btnTemaAzul = findViewById(R.id.btnTemaAzul);
        btnTemaMorao = findViewById(R.id.btnTemaMorao);
        btnTemaNaranja = findViewById(R.id.btnTemaNaranja);
        btnTemaRojo = findViewById(R.id.btnTemaRojo);
        btnTemaVerde = findViewById(R.id.btnTemaVerde);

        btnAplicarTemas = findViewById(R.id.btnAplicarTemaAjustes);
        btnCancelarTemas = findViewById(R.id.btnCancelarTemaAjustes);

        btnFondoCircuito = findViewById(R.id.btnFondoCircuito);
        btnFondoDoom = findViewById(R.id.btnFondoDoom);
        btnFondoHex = findViewById(R.id.btnFondoHex);
        btnFondoPkm = findViewById(R.id.btnFondoPkm);
        btnFondoOrange = findViewById(R.id.btnFondoOrange);
        btnFondoSunset = findViewById(R.id.btnFondoSunset);
        btnFondoSw = findViewById(R.id.btnFondoSw);
        btnFondoGameboy = findViewById(R.id.btnFondoGameboy);
        btnFondoCalle = findViewById(R.id.btnFondoCalle);
        btnFondoMandos = findViewById(R.id.btnFondoMandos);
        btnFondoMetal = findViewById(R.id.btnFondoMetal);
        btnFondoMarioAmarillo = findViewById(R.id.btnFondoMarioAmarillo);
        btnFondoMarioAzul = findViewById(R.id.btnFondoMarioAzul);
        btnFondoMarioNaranja = findViewById(R.id.btnFondoMarioNaranja);
        btnFondoMarioBlanco = findViewById(R.id.btnFondoMarioBlanco);

        btnAplicarFondos = findViewById(R.id.btnAplicarFondoAjustes);
        btnCancelarFondos = findViewById(R.id.btnCancelarFondoAjustes);

        llTemas = findViewById(R.id.llTemasAjustes);
        btnTemas = findViewById(R.id.btnCambiarTema);

        llFondo = findViewById(R.id.llFondosAjustes);
        btnFondos = findViewById(R.id.btnCambiarFondo);

        btnTemas.setOnClickListener(this);
        btnTemaVerde.setOnClickListener(this);
        btnTemaNaranja.setOnClickListener(this);
        btnTemaAzul.setOnClickListener(this);
        btnTemaRojo.setOnClickListener(this);
        btnTemaMorao.setOnClickListener(this);
        btnCancelarTemas.setOnClickListener(this);
        btnAplicarTemas.setOnClickListener(this);

        btnFondos.setOnClickListener(this);
        btnFondoCircuito.setOnClickListener(this);
        btnFondoDoom.setOnClickListener(this);
        btnFondoHex.setOnClickListener(this);
        btnFondoPkm.setOnClickListener(this);
        btnFondoOrange.setOnClickListener(this);
        btnFondoSunset.setOnClickListener(this);
        btnFondoSw.setOnClickListener(this);
        btnFondoGameboy.setOnClickListener(this);
        btnFondoCalle.setOnClickListener(this);
        btnFondoMandos.setOnClickListener(this);
        btnFondoMetal.setOnClickListener(this);
        btnFondoMarioAmarillo.setOnClickListener(this);
        btnFondoMarioAzul.setOnClickListener(this);
        btnFondoMarioNaranja.setOnClickListener(this);
        btnFondoMarioBlanco.setOnClickListener(this);
        btnAplicarFondos.setOnClickListener(this);
        btnCancelarFondos.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnAplicarTemas)) {
            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("colorTema").setValue(temaElegido);

            ((Game2dayApplication) getApplicationContext()).setColor(temaElegido);
            int colorTransOscuro;
            int colorTransMenos;
            int colorTransMenosMasMenosMasMasMenos;
            int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos;
            int colorrChilling;
            int colorrChillingTrans;
            int colorChilling;

            switch (temaElegido) {
                case "verde":
                    colorTransOscuro = R.color.verde_trans_oscuro;
                    colorTransMenos = R.color.verde_trans_menos;
                    colorTransMenosMasMenosMasMasMenos = R.color.verde_trans_menos_mas_menos_mas_mas_menos;
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.verde_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                    colorrChilling = R.color.verrde_chilling;
                    colorrChillingTrans = R.color.verrde_chilling_trans;
                    colorChilling = R.color.verde_chilling;
                    break;
                case "rojo":
                    colorTransOscuro = R.color.rojo_trans_oscuro;
                    colorTransMenos = R.color.rojo_trans_menos;
                    colorTransMenosMasMenosMasMasMenos = R.color.rojo_trans_menos_mas_menos_mas_mas_menos;
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.rojo_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                    colorrChilling = R.color.rrojo_chilling;
                    colorrChillingTrans = R.color.rrojo_chilling_trans;
                    colorChilling = R.color.rojo_chilling;
                    break;
                case "azul":
                    colorTransOscuro = R.color.azul_trans_oscuro;
                    colorTransMenos = R.color.azul_trans_menos;
                    colorTransMenosMasMenosMasMasMenos = R.color.azul_trans_menos_mas_menos_mas_mas_menos;
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.azul_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                    colorrChilling = R.color.azzul_chilling;
                    colorrChillingTrans = R.color.azzul_chilling_trans;
                    colorChilling = R.color.azul_chilling;
                    break;
                case "naranja":
                    colorTransOscuro = R.color.naranja_trans_oscuro;
                    colorTransMenos = R.color.naranja_trans_menos;
                    colorTransMenosMasMenosMasMasMenos = R.color.naranja_trans_menos_mas_menos_mas_mas_menos;
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.naranja_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                    colorrChilling = R.color.narranja_chilling;
                    colorrChillingTrans = R.color.narranja_chilling_trans;
                    colorChilling = R.color.naranja_chilling;
                    break;
                default:
                    colorTransOscuro = R.color.morao_trans_oscuro;
                    colorTransMenos = R.color.morao_trans_menos;
                    colorTransMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos;
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
                    colorrChilling = R.color.morrao_chilling;
                    colorrChillingTrans = R.color.morrao_chilling_trans;
                    colorChilling = R.color.morao_chilling;
            }

            Tema tema = new Tema(colorTransOscuro, colorTransMenos, colorTransMenosMasMenosMasMasMenos,
                    colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos, colorrChilling,
                    colorrChillingTrans, colorChilling);

            ((Game2dayApplication) getApplicationContext()).setTema(tema);

            Toast.makeText(this, R.string.tema_aplicado, Toast.LENGTH_SHORT);

            temasVisibles = false;
            itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llTemas.setVisibility(View.GONE);
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
        } else if (view.equals(btnAplicarFondos)) {
            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("fondo").setValue(fondoElegido);

            ((Game2dayApplication) getApplicationContext()).setFondo(fondoElegido);

            Toast.makeText(this, R.string.tema_aplicado, Toast.LENGTH_SHORT);

            fondosVisibles = false;
            itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llFondo.setVisibility(View.GONE);
            fondoElegido = ((Game2dayApplication) getApplicationContext()).getColor();
        } else if (view.equals(btnCancelarTemas)) {
            temasVisibles = false;
            itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llTemas.setVisibility(View.GONE);
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
        } else if (view.equals(btnCancelarFondos)) {
            fondosVisibles = false;
            itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llFondo.setVisibility(View.GONE);
            fondoElegido = ((Game2dayApplication) getApplicationContext()).getFondo();
        } else if (view.equals(btnTemas)) {

            if (temasVisibles) {
                itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
                llTemas.setVisibility(View.GONE);
                temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();

            } else {
                itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
                llFondo.setVisibility(View.GONE);
                fondoElegido = ((Game2dayApplication) getApplicationContext()).getFondo();
                fondosVisibles = false;

                itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemshown));

                llTemas.setVisibility(View.VISIBLE);

                switch (((Game2dayApplication) getApplicationContext()).getColor()) {
                    case "azul":
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.VISIBLE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;
                    case "naranja":
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.VISIBLE);
                        break;
                    case "rojo":
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.VISIBLE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;
                    case "verde":
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.VISIBLE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;
                    default:
                        underlineTemaMorao.setVisibility(View.VISIBLE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;

                }
            }
            temasVisibles = !temasVisibles;

        } else if (view.equals(btnFondos)) {

            if (fondosVisibles) {
                itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
                llFondo.setVisibility(View.GONE);
                fondoElegido = ((Game2dayApplication) getApplicationContext()).getFondo();

            } else {
                itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
                llTemas.setVisibility(View.GONE);
                temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
                temasVisibles = false;

                itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemshown));

                llFondo.setVisibility(View.VISIBLE);

                switch (((Game2dayApplication) getApplicationContext()).getFondo()) {
                    case "circuito":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.VISIBLE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "doom":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.VISIBLE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "orange":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.VISIBLE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "pkm":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.VISIBLE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "sunset":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.VISIBLE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "sw":
                        underlineFondoSw.setVisibility(View.VISIBLE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "gameboy":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.VISIBLE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "calle":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.VISIBLE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "mandos":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.VISIBLE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "metal":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.VISIBLE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "marioamarillo":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.VISIBLE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "marioazul":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.VISIBLE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "marionaranja":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.VISIBLE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;
                    case "marioblanco":
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.GONE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.VISIBLE);
                        break;
                    default:
                        underlineFondoSw.setVisibility(View.GONE);
                        underlineFondoSunset.setVisibility(View.GONE);
                        underlineFondoOrange.setVisibility(View.GONE);
                        underlineFondoPkm.setVisibility(View.GONE);
                        underlineFondoHex.setVisibility(View.VISIBLE);
                        underlineFondoDoom.setVisibility(View.GONE);
                        underlineFondoCircuito.setVisibility(View.GONE);
                        underlineFondoGameboy.setVisibility(View.GONE);
                        underlineFondoCalle.setVisibility(View.GONE);
                        underlineFondoMandos.setVisibility(View.GONE);
                        underlineFondoMetal.setVisibility(View.GONE);
                        underlineFondoMarioAmarillo.setVisibility(View.GONE);
                        underlineFondoMarioAzul.setVisibility(View.GONE);
                        underlineFondoMarioNaranja.setVisibility(View.GONE);
                        underlineFondoMarioBlanco.setVisibility(View.GONE);
                        break;


                }
            }
            fondosVisibles = !fondosVisibles;

        } else if (view.equals(btnTemaMorao)) {
            temaElegido = "morao";
            underlineTemaMorao.setVisibility(View.VISIBLE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        } else if (view.equals(btnTemaAzul)) {
            temaElegido = "azul";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.VISIBLE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        } else if (view.equals(btnTemaNaranja)) {
            temaElegido = "naranja";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.VISIBLE);

        } else if (view.equals(btnTemaVerde)) {
            temaElegido = "verde";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.VISIBLE);
            underlineTemaNaranja.setVisibility(View.GONE);

        } else if (view.equals(btnTemaRojo)) {
            temaElegido = "rojo";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.VISIBLE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        } else if (view.equals(btnFondoCircuito)) {
            fondoElegido = "circuito";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.VISIBLE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoDoom)) {
            fondoElegido = "doom";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.VISIBLE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoHex)) {
            fondoElegido = "hex";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.VISIBLE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoOrange)) {
            fondoElegido = "orange";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.VISIBLE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoPkm)) {
            fondoElegido = "pkm";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.VISIBLE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoSunset)) {
            fondoElegido = "sunset";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.VISIBLE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoSw)) {
            fondoElegido = "sw";
            underlineFondoSw.setVisibility(View.VISIBLE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoGameboy)) {
            fondoElegido = "gameboy";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.VISIBLE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoCalle)) {
            fondoElegido = "calle";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.VISIBLE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);

        } else if (view.equals(btnFondoMandos)) {
            fondoElegido = "mandos";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.VISIBLE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);

        } else if (view.equals(btnFondoMetal)) {
            fondoElegido = "metal";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.VISIBLE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoMarioAmarillo)) {
            fondoElegido = "marioamarillo";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.VISIBLE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoMarioAzul)) {
            fondoElegido = "marioazul";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.VISIBLE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoMarioNaranja)) {
            fondoElegido = "marionaranja";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.VISIBLE);
            underlineFondoMarioBlanco.setVisibility(View.GONE);
        } else if (view.equals(btnFondoMarioBlanco)) {
            fondoElegido = "marioblanco";
            underlineFondoSw.setVisibility(View.GONE);
            underlineFondoSunset.setVisibility(View.GONE);
            underlineFondoOrange.setVisibility(View.GONE);
            underlineFondoPkm.setVisibility(View.GONE);
            underlineFondoHex.setVisibility(View.GONE);
            underlineFondoDoom.setVisibility(View.GONE);
            underlineFondoCircuito.setVisibility(View.GONE);
            underlineFondoGameboy.setVisibility(View.GONE);
            underlineFondoCalle.setVisibility(View.GONE);
            underlineFondoMandos.setVisibility(View.GONE);
            underlineFondoMetal.setVisibility(View.GONE);
            underlineFondoMarioAmarillo.setVisibility(View.GONE);
            underlineFondoMarioAzul.setVisibility(View.GONE);
            underlineFondoMarioNaranja.setVisibility(View.GONE);
            underlineFondoMarioBlanco.setVisibility(View.VISIBLE);

        } else if (view.equals(btnSalir)) {
            Intent i = new Intent(SettingsActivity.this, PerfilPersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(HomeActivity.CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (llTemas.getVisibility() == View.VISIBLE) {
            itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llTemas.setVisibility(View.GONE);
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
            temasVisibles = false;
        } else if (llFondo.getVisibility() == View.VISIBLE) {
            itemFondos.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llFondo.setVisibility(View.GONE);
            fondoElegido = ((Game2dayApplication) getApplicationContext()).getFondo();
            fondosVisibles = false;
        } else if (llTemas.getVisibility() == View.GONE && llFondo.getVisibility() == View.GONE) {
            Intent i = new Intent(SettingsActivity.this, PerfilPersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(HomeActivity.CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
            startActivity(i);
            finish();
        }


    }
}