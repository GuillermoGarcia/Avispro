package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

public class AddSheetActivity extends AppCompatActivity {

  private Usuario usuario;
  private Personaje personaje = new Personaje();

  Button  cancelar;
  TextView  cultura;
  TextView  edad;
  Button    guardar;
  TextView  nombre;
  TextView  nivel;
  TextView  raza;
  TextView  procedencia;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_sheet);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
      this.usuario = (Usuario) bundle.getSerializable("usuario");

    cultura = findViewById(R.id.culture_sheet);
    edad = findViewById(R.id.age_sheet);
    nombre = findViewById(R.id.name_sheet);
    nivel = findViewById(R.id.level_sheet);
    raza = findViewById(R.id.race_sheet);
    procedencia = findViewById(R.id.origin_sheet);
    guardar = findViewById(R.id.save_button);

    guardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("AddSheetActivity","Guardar Pulsado");

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
            bundle.putBoolean("notificar", true);

            Intent intent = new Intent(AddSheetActivity.this, ListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        });
      }
    });
  }
}
