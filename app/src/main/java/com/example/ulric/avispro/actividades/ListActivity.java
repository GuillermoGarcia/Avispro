package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.example.ulric.avispro.adaptadores.sheetsListAdapter;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {

  private final List<Personaje> personajes = Collections.synchronizedList(new ArrayList<Personaje>());
  private Usuario usuario;
  private sheetsListAdapter adapter = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    // Creamos, si no existe, el directorio para guardar los avatars

    File file = new File(getApplicationContext().getFilesDir() + "/avatars/");
    if (!file.exists()) { file.mkdir(); }

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
      this.usuario = (Usuario) bundle.getSerializable("usuario");

    // Modificamos el ActionBar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Tus Personajes");

    // Creamos el adaptador que se manejara el listado de Personajes
    adapter = new sheetsListAdapter(this, R.layout.activity_list_sheet, personajes,
      new sheetsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Personaje character, int position) {
        // Pulsando sobre un personaje vamos a la Actividad que mostrara
        // los datos de dicho personaje
        Bundle bundle = new Bundle();
        bundle.putSerializable("personaje", character);
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(ListActivity.this, SheetActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        }
      }
    );

    // Refenrenciamos el RecyclerView y le asignamos el manejador del Layout y el adaptador
    final RecyclerView recycler = findViewById(R.id.listActivity);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    recycler.setAdapter(adapter);

    // Registramos el RecyclerView para tener acceso al menú contextual
    registerForContextMenu(recycler);

    // Cargamos los personajes del usuario
    cargarPersonajes(usuario.getPersonajes());

    // Referenciamos el botón flotante y le asignamos un listener
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(ListActivity.this, SheetActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
    inflater.inflate(R.menu.character_menu, menu);
  }

  /**
   * Nos permite realizar las acciones correspondientes a cada opción del menú contextual.
   * @param item, opción del menú pulsada
   * @return true
   */
  @Override
  public boolean onContextItemSelected(final MenuItem item) {

    switch(item.getItemId()) {
      case R.id.characterDelete:
        // Creamos un dialogo para confirmar que se desea borrar el personaje
        View view = getLayoutInflater().inflate(R.layout.dialog_delete_character,
                                           null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        String title = getText(R.string.character_delete) + " " +  personajes.get(adapter.getPosition()).getNombre();
        builder.setTitle(title).setView(view)
          .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
          })
          .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              adapter.borrarPersonaje(adapter.getPosition(), usuario);
              //Toast.makeText(ListActivity.this, getText(R.string.character_delete) + " "
              // + personajes.get(adapter.getPosition()).getNombre(), Toast.LENGTH_SHORT).show();
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
   * @return true
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    Bundle bundle = new Bundle();
    // Comprobar qué opción ha elegido el usuario
    switch(item.getItemId()) {

      case R.id.appExit:
        usuario.salir();
        Intent salir = new Intent(ListActivity.this, LoginActivity.class);
        startActivity(salir);
        break ;
      case R.id.userEdit:
        bundle.putSerializable("usuario", usuario);
        Intent settings = new Intent(ListActivity.this, SettingActivity.class);
        settings.putExtras(bundle);
        startActivity(settings);
        break ;
      case R.id.combatTurns:
        bundle.putSerializable("usuario", usuario);
        Intent combat = new Intent(ListActivity.this, ListCombatActivity.class);
        combat.putExtras(bundle);
        startActivity(combat);
        break ;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true ;
  }

  /**
  * Sobrecargamos el botón 'Atras' para evitar poder volver a la Actividad Login
  **/
  @Override
  public void onBackPressed(){ }

  private void cargarPersonajes(List<String> idPersonajes) {
    for (final String id : idPersonajes) {
      FirebaseFirestore.getInstance().collection("personajes").document(id).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("Usuario", "DocumentSnapshot data: " + document.getData());
                Personaje p = document.toObject(Personaje.class);
                //p.cargarAvatar(getApplicationContext(), false);
                personajes.add(p);
                Collections.sort(personajes);
                adapter.notifyItemInserted(personajes.size() - 1);
              } else {
                Log.d("Usuario", "No such document");
              }
            } else {
              Log.d("Usuario", "get failed with ", task.getException());
            }
          }
        });
    }
  }
}
