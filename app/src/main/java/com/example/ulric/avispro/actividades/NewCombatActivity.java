package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Combatiente;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewCombatActivity extends AppCompatActivity {

  private Usuario usuario;
  private Combate combate;
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
      if (bundle.containsKey("combate")) {
        this.combate = (Combate) bundle.getSerializable("combate");
      }
    }

    // Modificamos el ActionBar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Nuevo Combate");

    titulo = findViewById(R.id.combatTitleValue);
    descripcion = findViewById(R.id.descriptionEditText);

    final GridLayout gridLayout = findViewById(R.id.pnjsList);

    if (this.combate != null) {
      titulo.setText(combate.getNombre());
      descripcion.setText(combate.getDescripcion());
      for (String idPnj : combate.getIdPnjs()) {
        FirebaseFirestore.getInstance().collection("combatiente").document(idPnj).get()
          .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
                  Context contexto = getApplicationContext();
                  final Combatiente pnj = d.toObject(Combatiente.class);
                  final int childCount = gridLayout.getChildCount();
                  TextView pnjText = new TextView(contexto);
                  pnjText.setText(String.format("%s", pnj.getNombre()));
                  pnjText.setTextSize(22);
                  ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                  ((LinearLayout.LayoutParams) params).setMargins(0, 0, 10, 0);
                  pnjText.setLayoutParams(params);
                  ImageButton imageButton = new ImageButton(contexto);
                  imageButton.setImageResource(R.mipmap.ic_delete);
                  imageButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                  imageButton.setScaleX((float)0.75);
                  imageButton.setScaleY((float)0.75);
                  imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      borrarPnj(gridLayout, childCount, pnj);
                    }
                  });
                  LinearLayout linearLayout = new LinearLayout(contexto);
                  linearLayout.addView(pnjText, 0);
                  linearLayout.addView(imageButton, 1);
                  params = new LinearLayout.LayoutParams((gridLayout.getWidth() / 2), ViewGroup.LayoutParams.WRAP_CONTENT);
                  linearLayout.setLayoutParams(params);
                  gridLayout.addView(linearLayout, childCount);
                }
              }
            }
          });
      }
    }
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
                Combate c = (combate != null) ? new Combate() : combate;
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

  private void borrarPnj(final GridLayout gridLayout, final int childCount, final Combatiente pnj) {
    View child = gridLayout.getChildAt(childCount);
    View view = getLayoutInflater().inflate(R.layout.dialog_delete_npc, null, false);
    AlertDialog.Builder builder = new AlertDialog.Builder(NewCombatActivity.this);
    final TextView dialogText = view.findViewById(R.id.dialog_delete_npc_text);
    dialogText.setText(String.format("%s %s %s", R.string.dialog_delete_npc_text, pnj.getNombre(), "?"));
    builder.setTitle(R.string.dialog_delete_npc_title).setView(view)
        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) { }
        })
        .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            gridLayout.removeViewAt(childCount);
            combate.quitarPnj(pnj);
          }
        }).show();
  }
}
