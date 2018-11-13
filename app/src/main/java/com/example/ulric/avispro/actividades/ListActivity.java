package com.example.ulric.avispro.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Usuario;

public class ListActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      Usuario usuario = (Usuario) bundle.getSerializable("usuario") ;
      Toast.makeText(this, "Bienvenido/a, " + usuario.getAlias(), Toast.LENGTH_LONG).show();
    }

  }
}
