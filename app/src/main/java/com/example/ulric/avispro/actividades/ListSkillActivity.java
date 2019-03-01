package com.example.ulric.avispro.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.skillsListAdapter;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Habilidad;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSkillActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario   usuario;
  private List<Habilidad> habilidades = Collections.synchronizedList(new ArrayList<Habilidad>());
  private FloatingActionButton fab;
  private skillsListAdapter adapter = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario = (Usuario) bundle.getSerializable("usuario");
      this.personaje = (Personaje) bundle.getSerializable("personaje");
    }

    // Modificamos el ActionBar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Habilidades de " + this.personaje.getNombre());

    // Creamos el adaptador que manejara el listado de Habilidades
    adapter = new skillsListAdapter(this, R.layout.activity_list_skill, habilidades,
            new skillsListAdapter.OnItemClickListener() {
              @Override
              public void onItemClick(Habilidad habilidad, int position) {
                // Pulsando sobre un personaje vamos a la Actividad que mostrara
                // los datos de dicho personaje
                Bundle bundle = new Bundle();
                bundle.putSerializable("personaje", personaje);
                bundle.putSerializable("usuario", usuario);
                bundle.putSerializable("habilidad", habilidad);
                bundle.putSerializable("position", position);
                Intent skill = new Intent(ListSkillActivity.this, SkillActivity.class);
                skill.putExtras(bundle);
                startActivityForResult(skill, 0);
              }
            }
    );

    // Refenrenciamos el RecyclerView y le asignamos el manejador del Layout y el adaptador
    final RecyclerView recycler = findViewById(R.id.listActivity);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    recycler.setAdapter(adapter);

    // Registramos el RecyclerView para tener acceso al menú contextual
    registerForContextMenu(recycler);

    // Cargamos las habilidades del personaje
    cargarHabilidadesPersonaje();

    // Referenciamos el botón flotante y le asignamos un listener
    fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent intent = new Intent(ListSkillActivity.this, SkillActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
      }
    });
  }

  /**
   * Crea un menú contextual asociado a cada uno de los ítems
   * @param menu, referencia al menú contextual
   * @param v, referencia a la vista desde la que se ha lanzado el menú contextual
   * @param menuInfo, información del menú contextual
   */
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    // Le pasamos los datos al superior
    super.onCreateContextMenu(menu, v, menuInfo);
    // Inflamos el layout del menú Contextual
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.skill_item_menu, menu);
  }

  /**
   * Nos permite realizar las acciones correspondientes a cada opción del menú contextual.
   * @param item, opción del menú pulsada
   * @return
   */
  @Override
  public boolean onContextItemSelected(final MenuItem item) {

    switch(item.getItemId()) {
      case R.id.skillDelete:
        // Creamos un dialogo para confirmar que se desea borrar la habilidad
        View view = getLayoutInflater().inflate(R.layout.dialog_delete_skill,
                null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListSkillActivity.this);
        String title = getText(R.string.skill_delete) + " " +  habilidades.get(adapter.getPosition()).getNombre();
        builder.setTitle(title).setView(view)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    adapter.borrarHabilidad(adapter.getPosition(), personaje);
                    Log.d("Borrado Habilidad: ", adapter.getPosition()+"");
                    //Toast.makeText(ListActivity.this, getText(R.string.character_delete) + " " + personajes.get(adapter.getPosition()).getNombre(), Toast.LENGTH_SHORT).show();
                  }
                }).show();
        break ;

      default:
        return super.onContextItemSelected(item);
    }

    return true ;

  }

  /**
   * @param menu, menu de la actividad
   * @return super, regresamos la llamada al superior.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Obtenemos el inflador para inflar el menú
    getMenuInflater().inflate(R.menu.app_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * @param item, opción del menú seleccionada
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Bundle bundle = new Bundle();

    // Comprobar qué opción ha elegido el usuario
    switch(item.getItemId()) {

      case R.id.appExit:
        usuario.salir();
        Intent salir = new Intent(ListSkillActivity.this, LoginActivity.class);
        startActivity(salir);
        break;
      case R.id.userEdit:
        bundle.putSerializable("usuario", usuario);
        Intent settings = new Intent(ListSkillActivity.this, SettingActivity.class);
        settings.putExtras(bundle);
        startActivity(settings);
        break;
      case R.id.combatTurns:
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent combat = new Intent(ListSkillActivity.this, ListCombatActivity.class);
        combat.putExtras(bundle);
        startActivity(combat);
        break;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      Bundle bundle = data.getExtras();
      if (bundle != null){
        Habilidad habilidad = (Habilidad) bundle.getSerializable("habilidad");
        int position = (int) bundle.getSerializable("position");
        if (position == -1) {
          this.habilidades.add(habilidad);
          adapter.notifyItemChanged(this.habilidades.size() - 1);
        } else {
          this.habilidades.set(position, habilidad);
          adapter.notifyItemChanged(position);
        }
      }
    }
  }

  private void cargarHabilidadesPersonaje() {
    for(String idHabilidad: personaje.getIdHabilidades()) {
      FirebaseFirestore.getInstance().collection("habilidadPersonaje")
        .document(idHabilidad).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("Habilidad", "DocumentSnapshot data: " + document.getData());
                cargarHabilidad(document.toObject(Habilidad.class));
              } else {
                Log.d("Habilidad", "No such document");
              }
            } else {
              Log.d("Habilidad", "get failed with ", task.getException());
            }
          }
        });
    }
  }

  private void cargarHabilidad(final Habilidad h) {
    FirebaseFirestore.getInstance().collection("habilidad")
      .document(h.getIdHabilidad()).get()
      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
              Log.d("Habilidad", "DocumentSnapshot data: " + document.getData());
              Habilidad hab = document.toObject(Habilidad.class);
              hab.setValorBase(h.getValorBase());
              hab.setExtra(h.getExtra());
              hab.guardarHabilidadUsada(h.getHabilidadUsada());
              hab.setIdHabilidadPersonaje(h.getIdHabilidadPersonaje());
              hab.setPap(h.getPap());
              hab.calcularBonusHabilidad(personaje);
              habilidades.add(hab);
              adapter.notifyDataSetChanged();
            } else {
              Log.d("Habilidad", "No such document");
            }
          } else {
            Log.d("Habilidad", "get failed with ", task.getException());
          }
        }
      });

  }

}
