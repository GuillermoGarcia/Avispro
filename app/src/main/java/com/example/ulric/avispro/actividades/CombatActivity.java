package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.selectorListWithImageAdapter;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Combatiente;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CombatActivity extends AppCompatActivity {

  private Usuario           usuario;
  private Personaje         personaje;
  private int               position;
  private Combate           combate;
  private Combatiente       pj;
  private List<Combatiente> pjs;
  private TextView          numPjs;
  private Button            unirse, quitarse, combatir, gestion, volver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_combat);

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario =         (Usuario) bundle.getSerializable("usuario");
      this.personaje =       (Personaje) bundle.getSerializable("personaje");
      this.combate =         (Combate) bundle.getSerializable("combate");
      this.position =        bundle.getInt("position");
    }

    // Modificamos el ActionBar
    try {
      getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
      getSupportActionBar().setSubtitle("Combates");
    } catch (NullPointerException e) {
      Log.d("Combat Activity: ", e.getLocalizedMessage());
    }

    TextView titulo =        findViewById(R.id.combatTitle);
    TextView master =        findViewById(R.id.combatMaster);
    numPjs =                 findViewById(R.id.combatPjCountValue);
    TextView enCombate =     findViewById(R.id.inCombat);
    TextView enPreparacion = findViewById(R.id.noCombat);
    TextView turno =         findViewById(R.id.combatTurnValue);
    unirse =                 findViewById(R.id.combatJoin);
    quitarse =               findViewById(R.id.combatLeft);
    combatir =               findViewById(R.id.combatFight);
    Button iniciar =         findViewById(R.id.combatInit);
    gestion =                findViewById(R.id.combatPnj);
    volver =                 findViewById(R.id.combatCancel);
    if (personaje != null) {
      pj = new Combatiente(personaje.getIdPersonaje(), personaje.getNombre(),
          personaje.conseguirIniciativa());
    } else { cargarCombatiente(); }

    combatir.setVisibility(View.INVISIBLE);
    enCombate.setVisibility(View.INVISIBLE);
    enPreparacion.setVisibility(View.INVISIBLE);
    unirse.setVisibility(View.INVISIBLE);
    quitarse.setVisibility(View.INVISIBLE);
    iniciar.setVisibility(View.INVISIBLE);
    gestion.setVisibility(View.INVISIBLE);

    View sep = findViewById(R.id.separadorPnj);
    TextView pnjTitle = findViewById(R.id.pnjListTitle);
    sep.setVisibility(View.INVISIBLE);
    pnjTitle.setVisibility(View.INVISIBLE);

    titulo.setText(combate.getNombre());
    master.setText(String.format("%s %s", getText(R.string.combat_master), combate.getMaster()));
    numPjs.setText(String.format("%s", combate.getIdPjs().size()));
    turno.setText(String.format("%s", combate.getTurno()));

    if (combate.esMaster(usuario.getIdUsuario())) {
      iniciar.setVisibility(View.VISIBLE);
      gestion.setVisibility(View.VISIBLE);
      enPreparacion.setHeight(0);
      enCombate.setHeight(0);
      final GridLayout gridLayoutPnj = findViewById(R.id.pnjList);
      sep.setVisibility(View.VISIBLE);
      pnjTitle.setVisibility(View.VISIBLE);
      for (String idPnj : combate.getIdPnjs()) {
        FirebaseFirestore.getInstance().collection("combatiente").document(idPnj).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                  DocumentSnapshot d = task.getResult();
                  if (d.exists()){
                    Context contexto = getApplicationContext();
                    Combatiente pnj = d.toObject(Combatiente.class);
                    int childCount = gridLayoutPnj.getChildCount();
                    TextView pnjText = new TextView(contexto);
                    pnjText.setText(String.format("%s", pnj.getNombre()));
                    pnjText.setWidth((gridLayoutPnj.getWidth() / 2));
                    pnjText.setTextSize(22);
                    gridLayoutPnj.addView(pnjText, childCount);
                  }
                }
              }
            });
      }
    } else {
      if (combate.enMarcha()) {
        combatir.setVisibility(View.VISIBLE);
        enCombate.setVisibility(View.VISIBLE);
      } else {
        enPreparacion.setVisibility(View.VISIBLE);
        if ((pj != null) && (combate.estaUnido(pj.getIdCombatiente()))) {
          quitarse.setVisibility(View.VISIBLE);
        } else {
          unirse.setVisibility(View.VISIBLE);
        }
      }
    }

    final GridLayout gridLayoutPj = findViewById(R.id.pjList);
    for (final String idPj : combate.getIdPjs()) {
      FirebaseFirestore.getInstance().collection("combatiente").document(idPj).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot d = task.getResult();
              if (d.exists()) {
                Context contexto = getApplicationContext();
                Combatiente c = d.toObject(Combatiente.class);
                int childCount = gridLayoutPj.getChildCount();
                TextView pjText = new TextView(contexto);
                pjText.setText(String.format("%s", c.getNombre()));
                if ((pj != null) && (idPj.equals(pj.getIdCombatiente()))) {
                  pjText.setTextSize(26);
                } else {
                  pjText.setTextSize(22);
                }
                pjText.setWidth((gridLayoutPj.getWidth() / 2));
                gridLayoutPj.addView(pjText, childCount);
              }
            }
          }
        });
    }

    unirse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        View view = getLayoutInflater().inflate(R.layout.dialog_combat_join, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(CombatActivity.this);
        final Spinner pjList = view.findViewById(R.id.pj_list);
        final EditText speed = view.findViewById(R.id.speed_value);
        if (personaje != null) {
          LinearLayout select_pj = view.findViewById(R.id.select_pj);
          select_pj.setVisibility(View.INVISIBLE);
        } else {
          pjList.setAdapter(new selectorListWithImageAdapter(CombatActivity.this,
              R.layout.item_with_image_list_selector, pjs));
        }
        builder.setTitle(R.string.dialog_combat_join_title).setView(view)
          .setNegativeButton(R.string.dialog_combat_join_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
          })
          .setPositiveButton(R.string.dialog_combat_join_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (personaje == null) {
                pj = (Combatiente) pjList.getSelectedItem();
              }
              unirseAlCombate(speed.getText().toString());
              Context contexto = getApplicationContext();
              int childCount = gridLayoutPj.getChildCount();
              TextView pjText = new TextView(contexto);
              pjText.setText(String.format("%s", pj.getNombre()));
              pjText.setTextSize(22);
              gridLayoutPj.addView(pjText, childCount);
              pjText.setWidth((gridLayoutPj.getWidth() / 2));
              unirse.setVisibility(View.INVISIBLE);
              quitarse.setVisibility(View.VISIBLE);
              Toast.makeText(CombatActivity.this, R.string.combat_wait_start, Toast.LENGTH_LONG).show();
            }
          }).show();
      }
    });

    quitarse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        View view = getLayoutInflater().inflate(R.layout.dialog_combat_left, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(CombatActivity.this);
        builder.setTitle(R.string.dialog_combat_left_title).setView(view)
            .setNegativeButton(R.string.dialog_combat_left_cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) { }
            })
            .setPositiveButton(R.string.dialog_combat_left_ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                int i = gridLayoutPj.getChildCount() - 1;
                while ((i > 0 ) && !(((TextView)gridLayoutPj.getChildAt(i)).getText().equals(pj.getNombre()))) {
                  i--;
                }
                gridLayoutPj.removeViewAt(i);
                quitarseDelCombate();
                unirse.setVisibility(View.VISIBLE);
                quitarse.setVisibility(View.INVISIBLE);
                Toast.makeText(CombatActivity.this, R.string.combat_left, Toast.LENGTH_LONG).show();
              }
            }).show();
      }
    });

    combatir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!combate.estaUnido(pj.getIdCombatiente())) {
          View view = getLayoutInflater().inflate(R.layout.dialog_combat_join, null, false);
          AlertDialog.Builder builder = new AlertDialog.Builder(CombatActivity.this);
          final EditText speed = view.findViewById(R.id.speed_value);
          builder.setTitle(R.string.dialog_combat_join_title).setView(view)
            .setNegativeButton(R.string.dialog_combat_join_cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
              }
            })
            .setPositiveButton(R.string.dialog_combat_join_ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                unirseAlCombate(speed.getText().toString());
                entrarAlCombate();
              }
            }).show();
        } else {
          entrarAlCombate();
        }
      }
    });

    iniciar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        combate.iniciarTurno();
        entrarAlCombate();
      }
    });

    gestion.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("combate", combate);
        Intent combat = new Intent(CombatActivity.this, NewCombatActivity.class);
        combat.putExtras(bundle);
        startActivity(combat);
        finish();
      }
    });

    volver.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(RESULT_CANCELED, new Intent(CombatActivity.this, ListCombatActivity.class));
        finish();
      }
    });

  }


  /**
   * Llamada cuando la actual ventana de la actividad gana o pierde el foco.
   * @param hasFocus, boolean
   */
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    volver.setHeight(gestion.getHeight());
  }

  /**
   * Comprobar si alguno de los personajes del jugador ya está unido al combate y si lo está obtener
   * desde firebase el combatiente de ese personaje, en caso contrario, llamar al método
   * listadoPersonajes.
   */
  private void cargarCombatiente() {
    String idCombatiente = null;
    for (String idPersonaje : usuario.getPersonajes()) {
      if (combate.getIdPjs().contains(idPersonaje)) { idCombatiente = idPersonaje; }
    }
    if (idCombatiente != null) {
      FirebaseFirestore.getInstance().collection("combatiente").document(idCombatiente).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("CombatActivity: ", "Documento: " + document.getData());
                pj = document.toObject(Combatiente.class);
                unirse.setVisibility(View.INVISIBLE);
                quitarse.setVisibility(View.VISIBLE);
              } else {
                Log.e("CombatActivity: ", "No hay documento");
              }
            } else {
              Log.e("CombatActivity: ", "Firebase get failed with " + task.getException());
            }
          }
        });
    } else { listadoPersonajes(); }
  }

  /**
   * Una vez el master del combate inicie el combate, ir a la actividad de iniciativas del combate.
   */
  private void entrarAlCombate() {
    Bundle bundle = new Bundle();
    bundle.putSerializable("usuario", usuario);
    bundle.putSerializable("combate", combate);
    bundle.putSerializable("idPersonaje", personaje.getIdPersonaje());
    Intent combat = new Intent(CombatActivity.this, LeadActivity.class);
    combat.putExtras(bundle);
    startActivity(combat);
    finish();
  }

  /**
   * Cargar los personajes del usuario como posibles combatientes para elegir uno a la hora de unirse
   * al combate
   */
  private void listadoPersonajes() {
    this.pjs = new ArrayList<>();
    for(String idPersonaje: usuario.getPersonajes()) {
      FirebaseFirestore.getInstance().collection("personaje").document(idPersonaje).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("CombatActivity: ", "Documento: " + document.getData());
                Personaje p = document.toObject(Personaje.class);
                Combatiente c = new Combatiente(p.getAvatar(), p.getIdPersonaje(), p.getNombre(),
                                p.conseguirIniciativa(), 0);
                pjs.add(c);
              } else {
                Log.e("CombatActivity: ", "No hay documento");
              }
            } else {
              Log.e("CombatActivity: ", "Firebase get failed with " + task.getException());
            }
          }
        });
    }
  }

  /**
   * Dejar el combate.
   */
  private void quitarseDelCombate(){
    combate.quitarseDelCombate(this.pj);
    numPjs.setText(String.format("%s", combate.getIdPjs().size()));
  }

  /**
   * Unirse al combate.
   * @param speed, velocidad del arma recogido en el dialogo
   */
  private void unirseAlCombate(String speed) {
    this.pj.setVelocidadArma(Integer.parseInt(speed));
    this.pj.actualizarCombatiente();
    combate.unirseAlCombate(this.pj);
    numPjs.setText(String.format("%s", combate.getIdPjs().size()));
  }
}
