package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.sheetsListAdapter;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

  private final List<Personaje> personajes = new ArrayList<>();
  private Usuario usuario;
  private FloatingActionButton fab;
  private sheetsListAdapter adapter = null;
  private Boolean notificar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario = (Usuario) bundle.getSerializable("usuario");
      this.notificar = bundle.getBoolean("notificar");
    }

    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    //getSupportActionBar().setIcon(R.mipmap.ic_flixnet);
    getSupportActionBar().setSubtitle("Tus Personajes");

    final RecyclerView recycler = findViewById(R.id.listActivity);

    LinearLayoutManager manager = new LinearLayoutManager(this);

    adapter = new sheetsListAdapter(this, R.layout.activity_list_sheet, personajes,
      new sheetsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Personaje character, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("personaje", character);
        bundle.putSerializable("usuario", usuario);

        Intent intent = new Intent(ListActivity.this, SheetActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        }
      }
    );
    recycler.setLayoutManager(manager);
    recycler.setAdapter(adapter);

    registerForContextMenu(recycler);

    usuario.cargarPersonajes(new MyCallbackData() {
      @Override
      public void onCallbackData(Personaje personaje) {
        personajes.add(personaje);
        Log.d("Callback: ", "Personaje cargado: " + personaje.getIdPersonaje());
        adapter.notifyDataSetChanged();
      }
    });

    fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);

        Intent intent = new Intent(ListActivity.this, AddSheetActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

      }
    });
  }

  /**
   * Nos permite realizar las acciones correspondientes a cada opción del menú contextual.
   * @param item
   * @return
   */
  @Override
  public boolean onContextItemSelected(MenuItem item) {

    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    switch(item.getItemId()) {
      case 1:
        adapter.borrarPersonaje(item.getGroupId());
        Toast.makeText(this, "Borrar Personaje, " + item.getGroupId(), Toast.LENGTH_SHORT).show();
        break ;

      default:
        return super.onContextItemSelected(item);
    }

    return true ;

  }

  /**
   * @param menu, menu de la actividad
   * @return super, llamamos al padre.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Obtenemos el inflador para inflar el menú
    getMenuInflater().inflate(R.menu.app_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    // Comprobar qué opción ha elegido el usuario
    switch(item.getItemId()) {

      case R.id.appExit:
        usuario.salir();
        Intent intent = new Intent(ListActivity.this, LoginActivity.class);
        startActivity(intent);
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
}
