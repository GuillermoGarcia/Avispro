package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class TraitsActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario usuario;

  Map<String, TextView> bases, mods;
  ArrayList<String> keys = new ArrayList<String>() {{
    add("forTrait"); add("conTrait"); add("agiTrait"); add("desTrait"); add("refTrait"); add("intTrait");
    add("memTrait"); add("perTrait"); add("podTrait"); add("volTrait"); add("empTrait"); add("apaTrait");
  }};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_traits);
    try {
      getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
      getSupportActionBar().setSubtitle(R.string.traitsName);
    } catch (NullPointerException e) { Log.d("TraitsActivity", e.getLocalizedMessage()); }

    setTraits();


    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      this.usuario = (Usuario) bundle.getSerializable("usuario");
      if (Locale.getDefault().getDisplayLanguage().equals("español")) {
        getSupportActionBar().setSubtitle(getText(R.string.traitsNameOf) + " " + this.personaje.getNombre());
      } else {
        getSupportActionBar().setSubtitle(getText(R.string.traitsNameOf) + this.personaje.getNombre());
      }
      setValueTraits(this.personaje.getCaracteristicas(), this.personaje.getNivel()*2);
    }

  }

  /**
   * @param menu, menu de la actividad
   * @return super, regresamos la llamada al superior.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Obtenemos el inflador para inflar el menú
    getMenuInflater().inflate(R.menu.traits_menu, menu);
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

      case R.id.saveTraits:
        View view = getLayoutInflater().inflate(R.layout.dialog_generic_save,
            null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(TraitsActivity.this);
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
                personaje.setCaracteristicas(getValueTraits());
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario", usuario);
                bundle.putSerializable("personaje", personaje);
                Intent settings = new Intent(TraitsActivity.this, SheetActivity.class);
                settings.putExtras(bundle);
                startActivity(settings);
              }
            }).show();
        break ;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true ;
  }

  /**
   * Link Views of Traits with variables
   */
  private void setTraits(){

    Log.d("TraitsActivity",this.getRequestedOrientation() + "");
    TextView base, mod;
    this.bases = new ArrayMap<>();
    this.mods = new ArrayMap<>();
    for (String key : this.keys) {
      mod = null; base = null;
      switch (key){
        case "forTrait":
          base = findViewById(R.id.forTraitBase);
          mod = findViewById(R.id.forTraitMod);
          break;
        case "conTrait":
          base = findViewById(R.id.conTraitBase);
          mod = findViewById(R.id.conTraitMod);
          break;
        case "agiTrait":
          base = findViewById(R.id.agiTraitBase);
          mod = findViewById(R.id.agiTraitMod);
          break;
        case "desTrait":
          base = findViewById(R.id.desTraitBase);
          mod = findViewById(R.id.desTraitMod);
          break;
        case "refTrait":
          base = findViewById(R.id.refTraitBase);
          mod = findViewById(R.id.refTraitMod);
          break;
        case "intTrait":
          base = findViewById(R.id.intTraitBase);
          mod = findViewById(R.id.intTraitMod);
          break;
        case "memTrait":
          base = findViewById(R.id.memTraitBase);
          mod = findViewById(R.id.memTraitMod);
          break;
        case "perTrait":
          base = findViewById(R.id.perTraitBase);
          mod = findViewById(R.id.perTraitMod);
          break;
        case "podTrait":
          base = findViewById(R.id.podTraitBase);
          mod = findViewById(R.id.podTraitMod);
          break;
        case "volTrait":
          base = findViewById(R.id.volTraitBase);
          mod = findViewById(R.id.volTraitMod);
          break;
        case "empTrait":
          base = findViewById(R.id.empTraitBase);
          mod = findViewById(R.id.empTraitMod);
          break;
        case "apaTrait":
          base = findViewById(R.id.apaTraitBase);
          mod = findViewById(R.id.apaTraitMod);
          break;
        default: base = null; mod = null;
      }
      this.bases.put(key, base);
      this.mods.put(key, mod);
      base.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          final TextView vista = (TextView)v;
          View view = getLayoutInflater().inflate(R.layout.dialog_trait_value,
              null, false);
          AlertDialog.Builder builder = new AlertDialog.Builder(TraitsActivity.this);
          final NumberPicker trait = view.findViewById(R.id.dialog_trait_value);
          trait.setMinValue(1);
          trait.setMaxValue(40);
          trait.setValue(Integer.parseInt(vista.getText().toString()));
          builder.setTitle(R.string.dialog_trait_title).setView(view)
              .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
              })
              .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  vista.setText(""+trait.getValue());
                  //Toast.makeText(ListActivity.this, getText(R.string.character_delete) + " " + personajes.get(adapter.getPosition()).getNombre(), Toast.LENGTH_SHORT).show();
                }
              }).show();

        }
      });
      mod.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          final TextView vista = (TextView)v;
          View view = getLayoutInflater().inflate(R.layout.dialog_trait_value,
              null, false);
          AlertDialog.Builder builder = new AlertDialog.Builder(TraitsActivity.this);
          final NumberPicker trait = view.findViewById(R.id.dialog_trait_value);
          trait.setMinValue(1);
          trait.setMaxValue(40);
          trait.setValue(Integer.parseInt(vista.getText().toString()));
          builder.setTitle(R.string.dialog_trait_title).setView(view)
              .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
              })
              .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  vista.setText(""+trait.getValue());
                  //Toast.makeText(ListActivity.this, getText(R.string.character_delete) + " " + personajes.get(adapter.getPosition()).getNombre(), Toast.LENGTH_SHORT).show();
                }
              }).show();

        }
      });
    }
  }

  /**
   * Set Values from Character's Traits Map
   *
   * @param c, Maps of Traits from Character
   * @param m, min value of Traits
   */
  @SuppressWarnings("ConstantConditions")
  private void setValueTraits(Map<String, ArrayList<Long>> c, int m){

    String ckey;
    try {
      for (String key : this.keys) {
        ckey = Character.toUpperCase(key.charAt(0)) + key.substring(1, 3);
        Log.d("TraitsActivity", "ckey: " + ckey + ", Value:" + c.get(ckey));
        this.bases.get(key).setText(c.get(ckey).get(0).toString());
        this.mods.get(key).setText(c.get(ckey).get(1).toString());
      }
    } catch (NullPointerException e) { Log.d("TraitsActivity", e.getLocalizedMessage()); }

  }

  /**
   * Get Traits Values from View
   *
   * @return  Map with traits
   */
  @SuppressWarnings("ConstantConditions")
  private Map<String, ArrayList<Long>> getValueTraits() {

    String ckey;
    Map<String, ArrayList<Long>> c = new HashMap<>();
    ArrayList<Long> t;
    try {
      for (String key : this.keys) {
        ckey = Character.toUpperCase(key.charAt(0)) + key.substring(1, 3);
        t = new ArrayList<>();
        t.add(0,Long.parseLong(this.bases.get(key).getText().toString()));
        t.add(1,Long.parseLong(this.mods.get(key).getText().toString()));
        c.put(ckey, t);
      }
    } catch (NullPointerException e) { Log.d("TraitsActivity", e.getLocalizedMessage()); }
    return c;

  }

}
