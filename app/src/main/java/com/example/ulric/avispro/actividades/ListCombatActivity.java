package com.example.ulric.avispro.actividades;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.combatListAdapter;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Habilidad;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListCombatActivity extends AppCompatActivity {

  private Usuario usuario;
  private Personaje personaje;
  private List<Combate> combates = Collections.synchronizedList(new ArrayList<Combate>());
  private FloatingActionButton fab;
  private combatListAdapter adapter = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_combat);

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario =  (Usuario) bundle.getSerializable("usuario");
      if (bundle.containsKey("personaje")) {
        this.personaje = (Personaje) bundle.getSerializable("personaje");
      } else {
        this.personaje = null;
      }
    }

    // Modificamos el ActionBar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Combates");

    // Creamos el adaptador que se manejara el listado de Personajes
    String idPersonaje = (personaje != null) ? personaje.getIdPersonaje() : null;
    adapter = new combatListAdapter(this, R.layout.activity_list_combat_item, combates,
      new combatListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Combate combate, int position) {
          // Pulsando sobre un combate vamos a la Actividad que mostrara
          // los datos de dicho combate
          Bundle bundle = new Bundle();
          bundle.putSerializable("personaje", personaje);
          bundle.putSerializable("usuario", usuario);
          bundle.putSerializable("combate", combate);
          bundle.putSerializable("position", position);
          Intent intent = new Intent(ListCombatActivity.this, CombatActivity.class);
          intent.putExtras(bundle);
          startActivityForResult(intent, 0);
        }
      }, idPersonaje, usuario.getIdUsuario());

    // Refenrenciamos el RecyclerView y le asignamos el manejador del Layout y el adaptador
    final RecyclerView recycler = findViewById(R.id.listCombatActivity);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    recycler.setAdapter(adapter);

    // Registramos el RecyclerView para tener acceso al menú contextual
    registerForContextMenu(recycler);

    // Cargamos los combates
    cargarCombates();

    // Referenciamos el botón flotante y le asignamos un listener
    fab = findViewById(R.id.combatFab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(ListCombatActivity.this, NewCombatActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
      }
    });
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    cargarCombates();
    adapter.notifyDataSetChanged();
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      Bundle bundle = data.getExtras();
      if (bundle != null) {
        int p = bundle.getInt("position");
        if (p == 0) {
          this.combates.add((Combate) bundle.getSerializable("combate"));
          adapter.notifyItemChanged(this.combates.size() - 1);
        } else {
          this.combates.set(p, (Combate) bundle.getSerializable("combate"));
          adapter.notifyItemChanged(p);
        }
      }
    }
  }

  private void cargarCombates(){
    combates.clear();
    FirebaseFirestore.getInstance().collection("combate").get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              Log.d("Combates: ", document.getId() + " => " + document.getData());
              combates.add(document.toObject(Combate.class));
              adapter.notifyItemInserted(combates.size() - 1);
            }
          } else {
            Log.d("Combate", "get failed with ", task.getException());
          }
        }
      });
  }

}

