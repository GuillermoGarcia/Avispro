package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Combatiente;
import com.example.ulric.avispro.modelos.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewCombatActivity extends AppCompatActivity {

  private Usuario usuario;
  private List<Combatiente> pnjs = Collections.synchronizedList(new ArrayList<Combatiente>());
  private EditText titulo, descripcion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_combat);

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario = (Usuario) bundle.getSerializable("usuario");
    }

    // Modificamos el ActionBar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Nuevo Combate");

    titulo = findViewById(R.id.combatTitleValue);
    descripcion = findViewById(R.id.descriptionEditText);

    final GridLayout gridLayout = findViewById(R.id.pnjsList);

    ImageButton add = findViewById(R.id.addPnjImage);
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Context contexto = getApplicationContext();
        final int childCount = gridLayout.getChildCount();
        final TextView pnj = new TextView(contexto);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_pnj,
            null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(NewCombatActivity.this);
        String title = getText(R.string.dialog_new_pnj_title).toString();
        final EditText pnjName =      view.findViewById(R.id.pnjName);
        final NumberPicker pnjLead =  view.findViewById(R.id.pnjLead);
        pnjLead.setMinValue(1);
        pnjLead.setMaxValue(40);
        final NumberPicker pnjSpeed = view.findViewById(R.id.pnjSpeed);
        pnjSpeed.setMinValue(0);
        pnjSpeed.setMaxValue(10);
        builder.setTitle(title).setView(view)
          .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
          })
          .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              Combatiente c = new Combatiente(pnjName.getText().toString(),
                  Long.valueOf(pnjLead.getValue()), pnjSpeed.getValue());
              pnjs.add(c);
              pnj.setText(String.format("%s", pnjName.getText().toString()+" Iniciativa: " +
                                        pnjLead.getValue() + ", Arma: " + pnjSpeed.getValue()));
              pnj.setTextSize(22);
              gridLayout.addView(pnj, childCount);
            }
          }).show();
      }
    });

  }
  /**
   * @param menu, menu de la actividad
   * @return super, regresamos la llamada al superior.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Obtenemos el inflador para inflar el menú
    getMenuInflater().inflate(R.menu.combat_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * @param item, opción del menú seleccionada
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    // Comprobar qué opción ha elegido el usuario
    switch(item.getItemId()) {

      case R.id.saveCombat:
        View view = getLayoutInflater().inflate(R.layout.dialog_generic_save, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(NewCombatActivity.this);
        final TextView dialogText = view.findViewById(R.id.dialog_generic_save);
        dialogText.setText(R.string.dialog_save_combat);
        builder.setTitle(R.string.dialog_save_combat_title).setView(view)
            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) { }
            })
            .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> idPnjs = new ArrayList<>();
                for (Combatiente pnj : pnjs){
                  pnj.guardarCombatiente();
                  idPnjs.add(pnj.getIdCombatiente());
                }
                Combate c = new Combate();
                c.setNombre(titulo.getText().toString());
                c.setDescripcion(descripcion.getText().toString());
                c.setIdPjs(idPnjs);
                c.setMaster(usuario.getAlias());
                c.setIdMaster(usuario.getIdUsuario());
                c.guardarCombate();
                Bundle bundle = new Bundle();
                bundle.putSerializable("combate", c);
                Intent combate = new Intent(NewCombatActivity.this, ListCombatActivity.class);
                combate.putExtras(bundle);
                startActivity(combate);
                finish();
              }
            }).show();
        break ;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true ;
  }

}
