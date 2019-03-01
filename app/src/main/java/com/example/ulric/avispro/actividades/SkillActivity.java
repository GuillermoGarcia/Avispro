package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.interfaces.MyCallbackData;
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

public class SkillActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario usuario;
  private Habilidad habilidad;
  private int position;
  final private ArrayList<String> habilidades = new ArrayList<>();

  TextView base, bp, bs, extra, pap, total;
  AutoCompleteTextView nombre;
  ImageButton mas, menos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_skill);


    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      this.usuario = (Usuario) bundle.getSerializable("usuario");
      this.habilidad = (Habilidad) bundle.getSerializable("habilidad");
      this.position = (int) bundle.getInt("position", -1);
    }

    nombre = findViewById(R.id.skillName);
    base =   findViewById(R.id.skillBaseValue);
    bp =     findViewById(R.id.skillPrimaryBonusValue);
    bs =     findViewById(R.id.skillSecundaryBonusValue);
    extra =  findViewById(R.id.skillExtraValue);
    pap =    findViewById(R.id.skillPapValue);
    total =  findViewById(R.id.skillTotalValue);
    mas =    findViewById(R.id.skillBasePlusValue);
    menos =  findViewById(R.id.skillBaseMinusValue);

    if (habilidad != null) {
      nombre.setText(habilidad.getNombre());
      nombre.setEnabled(false);
      base.setText(habilidad.getValorBase()+"");
      bp.setText(habilidad.obtenerValorBonusPrincipal());
      bs.setText(habilidad.obtenerValorBonusSecundario());
      extra.setText(habilidad.getExtra()+"");
      pap.setText(habilidad.getPap()+"");
      total.setText(habilidad.obtenerValorTotal());
    } else {
      habilidad = new Habilidad();
      loadSkills();
      base.setText(R.string.skill_cero);
      bp.setText(R.string.skill_cero);
      bs.setText(R.string.skill_cero);
      extra.setText(R.string.skill_cero);
      pap.setText(R.string.skill_cero);
      total.setText(R.string.skill_cero);
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, habilidades);
      nombre.setAdapter(adapter);
      nombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          habilidad.setIdHabilidad(nombre.getText().toString());
          habilidad.obtenerHabilidadGeneral(new MyCallbackData() {
            @Override
            public void onCallbackData(Object objeto) {
              habilidad = (Habilidad) objeto;
              habilidad.calcularBonusHabilidad(personaje);
              habilidad.setValorBase(Integer.parseInt(base.getText().toString()));
              bp.setText(habilidad.obtenerValorBonusPrincipal());
              bs.setText(habilidad.obtenerValorBonusSecundario());
              total.setText(habilidad.obtenerValorTotal());
            }
          });
        }
      });
    }

    mas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int valor = Integer.parseInt(base.getText().toString());
        if (valor < 20) {
          base.setText(String.valueOf(valor + 1));
          habilidad.setValorBase(Integer.parseInt(base.getText().toString()));
          habilidad.calcularBonusHabilidad(personaje);
          total.setText(habilidad.obtenerValorTotal());
        }
      }
    });

    menos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int valor = Integer.parseInt(base.getText().toString());
        if (valor > 0) {
          base.setText(String.valueOf(valor - 1));
          habilidad.setValorBase(Integer.parseInt(base.getText().toString()));
          habilidad.calcularBonusHabilidad(personaje);
          total.setText(habilidad.obtenerValorTotal());
        }
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
    getMenuInflater().inflate(R.menu.skill_menu, menu);
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

      case R.id.saveSkill:
        View view = getLayoutInflater().inflate(R.layout.dialog_generic_save, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(SkillActivity.this);
        final TextView dialogText = view.findViewById(R.id.dialog_generic_save);
        dialogText.setText(R.string.dialog_save_trait);
        builder.setTitle(R.string.dialog_save_trait_title).setView(view)
            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) { }
            })
            .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                habilidad.guardarHabilidad(new MyCallbackData() {
                  @Override
                  public void onCallbackData(Object objeto) {
                    personaje.anadirIdHabilidad((String)objeto);
                  }
                });
                Bundle bundle = new Bundle();
                bundle.putSerializable("habilidad", habilidad);
                bundle.putSerializable("position", position);
                Intent skill = new Intent(SkillActivity.this, ListSkillActivity.class);
                skill.putExtras(bundle);
                setResult(RESULT_OK, skill);
                finish();
              }
            }).show();
        break ;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true ;
  }

  /**
   * Cargamos desde Firebase las habilidades
   */
  private void loadSkills(){
    FirebaseFirestore.getInstance().collection("habilidad").get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              habilidades.add(document.getId());
              // Log.d("SkillActivity", document.getId() + " => " + document.getData());
            }

          } else {
            Log.d("Usuario", "get failed with ", task.getException());
          }
       }
        });
  }
}