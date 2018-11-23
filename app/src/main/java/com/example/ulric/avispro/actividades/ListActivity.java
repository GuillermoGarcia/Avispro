package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.sheetsListAdapter;
import com.example.ulric.avispro.interfaces.MyCallbackPersonaje;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

  private final List<Personaje> personajes = new ArrayList<>();
  private Usuario usuario;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.usuario = (Usuario) bundle.getSerializable("usuario");
      Toast.makeText(this, "Bienvenido/a, " + this.usuario.getAlias(), Toast.LENGTH_LONG).show();
    }

    usuario.cargarPersonajes(personajes){ }//, new MyCallbackPersonaje() {
/*    @Override
      public void onCallback(Personaje personaje) {
        Log.d("Callback: ", "Personaje cargado: " + personaje.getIdPersonaje());
      }
    });*/

    RecyclerView recycler = findViewById(R.id.listActivity);

    LinearLayoutManager manager = new LinearLayoutManager(this);

    recycler.setLayoutManager(manager);

    sheetsListAdapter adapter = new sheetsListAdapter(this, R.layout.activity_list_sheet, personajes,
      new sheetsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Personaje character, int position) {

          Bundle bundle = new Bundle();
          bundle.putSerializable("personaje", character);

          Intent intent = new Intent(ListActivity.this, SheetActivity.class);
          intent.putExtras(bundle);
          startActivity(intent);

        }
      }

    );

    recycler.setAdapter(adapter);

  }
}
