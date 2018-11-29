package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.ulric.avispro.R;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import static java.lang.String.valueOf;

public class SheetActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario   usuario;

  Button    cancelar, guardar;
  TextView  cultura, edad, nombre, nivel, raza, procedencia;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sheet);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      this.usuario = (Usuario) bundle.getSerializable("usuario");
    }

    cultura = findViewById(R.id.etCultureCharacter);
    edad = findViewById(R.id.etAgeCharacter);
    nombre = findViewById(R.id.etNameCharacter);
    nivel = findViewById(R.id.etLevelCharacter);
    raza = findViewById(R.id.etRaceCharacter);
    procedencia = findViewById(R.id.etOriginCharacter);
    guardar = findViewById(R.id.save_button);
    cancelar = findViewById(R.id.cancel_button);

    if (personaje != null) {
      cultura.setText(personaje.getCultura());
      edad.setText(valueOf(personaje.getEdad()));
      nombre.setText(personaje.getNombre());
      nivel.setText(valueOf(personaje.getNivel()));
      raza.setText(personaje.getRaza());
      procedencia.setText(personaje.getProcedencia());
    } else {
      personaje = new Personaje();
    }

    guardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Guardar Pulsado");

        personaje.setCultura(cultura.getText().toString().trim());
        personaje.setEdad(Integer.parseInt(edad.getText().toString().trim()));
        personaje.setNombre(nombre.getText().toString().trim());
        personaje.setNivel(Integer.parseInt(nivel.getText().toString().trim()));
        personaje.setRaza(raza.getText().toString().trim());
        personaje.setProcedencia(procedencia.getText().toString().trim());

        personaje.setPersonaje(usuario, new MyCallbackData() {
          @Override
          public void onCallbackData(Personaje personaje) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("usuario", usuario);

            Intent intent = new Intent(SheetActivity.this, ListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        });

      }
    });

    cancelar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(SheetActivity.this, ListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

  }
}
