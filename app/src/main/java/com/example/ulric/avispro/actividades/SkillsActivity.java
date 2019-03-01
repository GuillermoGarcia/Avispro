package com.example.ulric.avispro.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

public class SkillsActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario   usuario;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_skills);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      this.usuario = (Usuario) bundle.getSerializable("usuario");
    }
  }
}
