package com.example.ulric.avispro.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    if (personaje != null) {
      cultura.setText(personaje.getCultura());
      edad.setText(personaje.getEdad()+"");
      nombre.setText(personaje.getNombre());
      nivel.setText(personaje.getNivel()+"");
      raza.setText(personaje.getRaza());
      procedencia.setText(personaje.getProcedencia());
    }

  }
}
