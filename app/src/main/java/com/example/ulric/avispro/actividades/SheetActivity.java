package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ulric.avispro.R;
import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static java.lang.String.valueOf;

public class SheetActivity extends AppCompatActivity {

  private Personaje personaje;
  private Usuario   usuario;

  TextView  cultura, edad, nombre, nivel, raza, procedencia;
  ImageView avatar;

  static final int REQUEST_IMAGE_GET = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sheet);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      this.personaje = (Personaje) bundle.getSerializable("personaje");
      this.usuario = (Usuario) bundle.getSerializable("usuario");
    }

    this.avatar =             findViewById(R.id.iavatar);
    LinearLayout editAvatar = findViewById(R.id.editAvatar);
    this.cultura =            findViewById(R.id.etCultureCharacter);
    this.edad =               findViewById(R.id.etAgeCharacter);
    this.nombre =             findViewById(R.id.etNameCharacter);
    this.nivel =              findViewById(R.id.etLevelCharacter);
    this.raza =               findViewById(R.id.etRaceCharacter);
    this.procedencia =        findViewById(R.id.etOriginCharacter);
    Button guardar =          findViewById(R.id.save_button);
    Button cancelar =         findViewById(R.id.cancel_button);
    ImageButton traits =      findViewById(R.id.traitsCharacter);
    ImageButton skills =      findViewById(R.id.skillsCharacter);
    ImageButton equip =       findViewById(R.id.equipCharacter);

    if (personaje != null) {
      cultura.setText(personaje.getCultura());
      edad.setText(valueOf(personaje.getEdad()));
      nombre.setText(personaje.getNombre());
      nivel.setText(valueOf(personaje.getNivel()));
      raza.setText(personaje.getRaza());
      procedencia.setText(personaje.getProcedencia());
      if (!personaje.getAvatar().equals("")) {
        Uri avatarUri = personaje.conseguirAvatarUri(getApplicationContext());
        Picasso.with(SheetActivity.this).load(avatarUri)
               .transform(new CropCircleTransformation()).fit().centerCrop().into(avatar);
      }
    } else {
      personaje = new Personaje();
    }

    guardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Guardar Pulsado");

        personaje.setCultura(cultura.getText().toString().trim());
        personaje.setEdad(Integer.parseInt(edad.getText().toString().trim()));
        personaje.setNombre(nombre.getText().toString().trim());
        personaje.setNivel(Integer.parseInt(nivel.getText().toString().trim()));
        personaje.setRaza(raza.getText().toString().trim());
        personaje.setProcedencia(procedencia.getText().toString().trim());

        personaje.guardarPersonaje(usuario, new MyCallbackData() {
          @Override
          public void onCallbackData(Object objeto) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("usuario", usuario);
            Intent intent = new Intent(SheetActivity.this, ListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        });

      }
    });

    cancelar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Cancelar Pulsado");

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(SheetActivity.this, ListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    traits.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Caracteristicas Pulsado");

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent intent = new Intent(SheetActivity.this, TraitsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    skills.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Caracteristicas Pulsado");

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent intent = new Intent(SheetActivity.this, ListSkillActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    equip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("SheetActivity","Caracteristicas Pulsado");

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent intent = new Intent(SheetActivity.this, EquipActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    editAvatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(intent, REQUEST_IMAGE_GET);
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
    getMenuInflater().inflate(R.menu.app_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * @param item, opción del menú seleccionada
   * @return boolean, true si correcto.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    Bundle bundle = new Bundle();
    // Comprobar qué opción ha elegido el usuario
    switch(item.getItemId()) {

      case R.id.appExit:
        usuario.salir();
        Intent salir = new Intent(SheetActivity.this, LoginActivity.class);
        startActivity(salir);
        break ;
      case R.id.userEdit:
        bundle.putSerializable("usuario", usuario);
        Intent settings = new Intent(SheetActivity.this, SettingActivity.class);
        settings.putExtras(bundle);
        startActivity(settings);
        break ;
      case R.id.combatTurns:
        bundle.putSerializable("usuario", usuario);
        bundle.putSerializable("personaje", personaje);
        Intent combat = new Intent(SheetActivity.this, ListCombatActivity.class);
        combat.putExtras(bundle);
        startActivity(combat);
        break ;

      default :
        return super.onOptionsItemSelected(item);
    }

    return true ;
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d("Personaje: ", "onRestart");
    personaje.recargarPersonaje(personaje);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
      Uri avatarUri = data.getData();
      Picasso.with(SheetActivity.this).load(avatarUri)
             .transform(new CropCircleTransformation()).fit().centerCrop().into(avatar);
      if (avatarUri != null) {
        Cursor cu = null;
        try {
          cu = getContentResolver().query(avatarUri, null, null, null, null);
          if (cu != null) {
            cu.moveToFirst();
            personaje.guardarNuevoAvatar(getContentResolver().openFileDescriptor(avatarUri, "r"),
                cu.getString(cu.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
            cu.close();
          }
        } catch (FileNotFoundException e) {
          e.printStackTrace();
          Log.e("SheetActivity: ", "File not found.");
        }
      }
    }
  }
}
