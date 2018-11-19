package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.sheetsListAdapter;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

  private List<Personaje> personajes;
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

    this.personajes = new ArrayList<Personaje>() {{
        add(new Personaje("", "Dunedain", 27, usuario.getIdUsuario(),
            1, "Siggurd", 2, "Gondor", "Edain"));
        add(new Personaje("", "Orcos", 21, usuario.getIdUsuario(),
                2, "Zradrur", 2, "Morgul", "Goblin"));
      }};

    RecyclerView recycler = findViewById(R.id.listActivity);

    GridLayoutManager manager = new GridLayoutManager(this, 1);

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
