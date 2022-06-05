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
    LinearLayout llTemas, btnTemas;
    LinearLayout btnTemaAzul, btnTemaMorao, btnTemaRojo, btnTemaVerde, btnTemaNaranja;
    ImageView itemTemas;
    TextView btnCancelarTemas, btnAplicarTemas;

    private FirebaseAuth mAuth;
    private DatabaseReference bdd;

    boolean temasVisibles = false;
    String temaElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        bdd = FirebaseDatabase.getInstance().getReference();

        temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();

        itemTemas = findViewById(R.id.itemTemasAjustes);
        underlineTemaAzul = findViewById(R.id.underlineTemaAzul);
        underlineTemaRojo = findViewById(R.id.underlineTemaRojo);
        underlineTemaVerde = findViewById(R.id.underlineTemaVerde);
        underlineTemaMorao = findViewById(R.id.underlineTemaMorao);
        underlineTemaNaranja = findViewById(R.id.underlineTemaNaranja);

        btnTemaAzul = findViewById(R.id.btnTemaAzul);
        btnTemaMorao = findViewById(R.id.btnTemaMorao);
        btnTemaNaranja = findViewById(R.id.btnTemaNaranja);
        btnTemaRojo = findViewById(R.id.btnTemaRojo);
        btnTemaVerde = findViewById(R.id.btnTemaVerde);
        btnAplicarTemas = findViewById(R.id.btnAplicarTemaAjustes);
        btnCancelarTemas = findViewById(R.id.btnCancelarTemaAjustes);


        llTemas = findViewById(R.id.llTemasAjustes);
        btnTemas = findViewById(R.id.btnCambiarTema);

        btnTemas.setOnClickListener(this);
        btnTemaVerde.setOnClickListener(this);
        btnTemaNaranja.setOnClickListener(this);
        btnTemaAzul.setOnClickListener(this);
        btnTemaRojo.setOnClickListener(this);
        btnTemaMorao.setOnClickListener(this);
        btnCancelarTemas.setOnClickListener(this);
        btnAplicarTemas.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnAplicarTemas)) {
            bdd.child("Users").child(mAuth.getCurrentUser().getUid()).child("colorTema").setValue(temaElegido);

            ((Game2dayApplication) getApplicationContext()).setColor(temaElegido);
            int colorTransOscuro = R.color.morao_trans_oscuro;
            int colorTransMenos = R.color.morao_trans_menos;
            int colorTransMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos;
            int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = R.color.morao_trans_menos_mas_menos_mas_mas_menos_menos_mas_menos_mas_mas_menos;
            int colorrChilling = R.color.morrao_chilling;
            int colorrChillingTrans = R.color.morrao_chilling_trans;
            int colorChilling = R.color.morao_chilling;

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

            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
            temasVisibles = false;
            itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llTemas.setVisibility(View.GONE);
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
        } else if (view.equals(btnCancelarTemas)) {
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
            temasVisibles = false;
            itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
            llTemas.setVisibility(View.GONE);
            temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();
        } else if (view.equals(btnTemas)) {

            if (temasVisibles) {
                itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemhidden));
                llTemas.setVisibility(View.GONE);
                temaElegido = ((Game2dayApplication) getApplicationContext()).getColor();

            } else {
                itemTemas.setImageDrawable(getResources().getDrawable(R.drawable.itemshown));

                llTemas.setVisibility(View.VISIBLE);

                switch (((Game2dayApplication) getApplicationContext()).getColor()) {
                    case "azul" :
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.VISIBLE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;
                    case "naranja" :
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.GONE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.VISIBLE);
                        break;
                    case "rojo" :
                        underlineTemaMorao.setVisibility(View.GONE);
                        underlineTemaAzul.setVisibility(View.GONE);
                        underlineTemaRojo.setVisibility(View.VISIBLE);
                        underlineTemaVerde.setVisibility(View.GONE);
                        underlineTemaNaranja.setVisibility(View.GONE);
                        break;
                    case "verde" :
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

        } else if (view.equals(btnTemaMorao)) {
            temaElegido = "morao";
            underlineTemaMorao.setVisibility(View.VISIBLE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        }else if (view.equals(btnTemaAzul)) {
            temaElegido = "azul";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.VISIBLE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        }else if (view.equals(btnTemaNaranja)) {
            temaElegido = "naranja";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.VISIBLE);

        }else if (view.equals(btnTemaVerde)) {
            temaElegido = "verde";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.GONE);
            underlineTemaVerde.setVisibility(View.VISIBLE);
            underlineTemaNaranja.setVisibility(View.GONE);

        }else if (view.equals(btnTemaRojo)) {
            temaElegido = "rojo";
            underlineTemaMorao.setVisibility(View.GONE);
            underlineTemaAzul.setVisibility(View.GONE);
            underlineTemaRojo.setVisibility(View.VISIBLE);
            underlineTemaVerde.setVisibility(View.GONE);
            underlineTemaNaranja.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent (SettingsActivity.this, PerfilPersonalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(HomeActivity.CLAVE_USUARIO, mAuth.getCurrentUser().getUid());
        startActivity(i);
        finish();

    }
}