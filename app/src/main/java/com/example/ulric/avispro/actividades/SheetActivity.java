package com.example.ulric.avispro.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Personaje;

public class SheetActivity extends AppCompatActivity {

  TextView  cultura;
  TextView  edad;
  TextView  nombre;
  TextView  nivel;
  TextView  raza;
  Personaje personaje;
  TextView  procedencia;
  Button    guardar;
  Button    cancelar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sheet);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      Toast.makeText(this, "Bienvenido/a, " + this.personaje.getNombre(), Toast.LENGTH_LONG).show();
    }

    cultura = findViewById(R.id.culture_sheet);
    edad = findViewById(R.id.age_sheet);
    nombre = findViewById(R.id.name_sheet);
    nivel = findViewById(R.id.level_sheet);
    raza = findViewById(R.id.race_sheet);
    procedencia = findViewById(R.id.origin_sheet);
    guardar = findViewById(R.id.save_button);

    if (personaje != null) {
      cultura.setText(personaje.getCultura());
      edad.setText(personaje.getEdad()+"");
      nombre.setText(personaje.getNombre());
      nivel.setText(personaje.getNivel()+"");
      raza.setText(personaje.getRaza());
      procedencia.setText(personaje.getProcedencia());
    }

    guardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        personaje.setCultura(cultura.getText().toString().trim());
        personaje.setEdad(Integer.parseInt(edad.getText().toString().trim()));
        personaje.setNombre(nombre.getText().toString().trim());
        personaje.setNivel(Integer.parseInt(nivel.getText().toString().trim()));
        personaje.setRaza(raza.getText().toString().trim());
        personaje.setProcedencia(procedencia.getText().toString().trim());

      }
    });

  }
}
