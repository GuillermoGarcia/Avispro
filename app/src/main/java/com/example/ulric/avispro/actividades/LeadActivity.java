package com.example.ulric.avispro.actividades;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.adaptadores.selectorListWithImageAdapter;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Combatiente;
import com.example.ulric.avispro.modelos.Usuario;

public class LeadActivity extends AppCompatActivity {

  private Combate     combate;
  private String      idPersonaje;
  private Usuario     usuario;
  private Combatiente combatiente;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lead);

    // Recogemos los datos de usuario pasados de una actividad anterior
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.combate =     (Combate) bundle.getSerializable("combate");
      this.idPersonaje = bundle.getString("idPersonaje");
      this.usuario =     (Usuario) bundle.getSerializable("usuario");
    }

    // Modificamos el ActionBar
    try {
      getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
      getSupportActionBar().setSubtitle("Combate - Orden de Iniciativa");
    } catch (NullPointerException e) {
      Log.d("Combat Activity: ", e.getLocalizedMessage());
    }

    combatiente = combate.turnoDe();

    TextView leadTitle = findViewById(R.id.leadTitle);
    leadTitle.setText(combate.getNombre());
    TextView characterName = findViewById(R.id.characterName);
    characterName.setText(combatiente.getNombre());
    if(combatiente.tieneAvatar()) {
      ImageView characterAvatar = findViewById(R.id.characterAvatar);
      characterAvatar.setImageURI(Uri.parse(combatiente.getAvatar()));
    }
    Spinner charactersList = findViewById(R.id.charactersList);
    charactersList.setAdapter(new selectorListWithImageAdapter(LeadActivity.this,
        R.layout.item_with_image_list_selector, combate.listadoCombatientes()));
    LinearLayout acciones = findViewById(R.id.characterActions);
    ProgressBar progressBar = findViewById(R.id.progressBar);
    progressBar.setVisibility(View.INVISIBLE);

    if (combate.getIdMaster().equals(usuario.getIdUsuario())) {
      if (!combate.esPj(combatiente.getIdCombatiente())) {
      }
    } else if (combatiente.getIdCombatiente().equals(idPersonaje)) {
      acciones.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.INVISIBLE);
    } else {
      acciones.setVisibility(View.INVISIBLE);
      progressBar.setVisibility(View.VISIBLE);
    }

  }

  public void onRadioButtonClicked(View view) {
    // Is the button now checked?
    boolean checked = ((RadioButton) view).isChecked();

    // Check which radio button was clicked
    switch(view.getId()) {
      case R.id.attackAction:
        if (checked)
          // Pirates are the best
          break;
      case R.id.moveAction:
        if (checked)
          // Ninjas rule
          break;
      case R.id.delayAction:
        if (checked)
          // Ninjas rule
          break;
      case R.id.aimAction:
        if (checked)
          // Ninjas rule
          break;
    }
  }

}
