package com.example.ulric.avispro.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

  private Button btnSave, btnCancel;
  private EditText ema, pas, npas, npasr, ali;
  private Boolean correcto = true;
  private FirebaseAuth mAuth;

  private Usuario usuario;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      this.usuario = (Usuario) bundle.getSerializable("usuario");
    }
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
    getSupportActionBar().setSubtitle("Edición Datos de Usuario");



    btnSave = findViewById(R.id.boton_save);
    btnCancel = findViewById(R.id.boton_cancel);

    ema = findViewById(R.id.etUsuario);
    pas = findViewById(R.id.etContrasena);
    npas = findViewById(R.id.etNuevaContrasena);
    npasr = findViewById(R.id.etNuevaContrasenaR);
    ali = findViewById(R.id.etAlias);

    ema.setText(usuario.getCorreo());
    ali.setText(usuario.getAlias());

    setupFloatingLabelError();

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (checkNewPasswordError()) {

          // Obtenemos los valores para el acceso del usuario
          final String correo = ema.getText().toString().trim();
          final String clave = pas.getText().toString().trim();
          final String nclave = npas.getText().toString().trim();
          final String alias = ali.getText().toString().trim();

          // Creamos el usuario nuevo
          mAuth = FirebaseAuth.getInstance();
          mAuth.signInWithEmailAndPassword(usuario.getCorreo(), clave)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                  // Cambio de Correo Electrónico
                  if (!correo.equals(usuario.getCorreo())){
                    mAuth.getCurrentUser().updateEmail(correo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                          FirebaseFirestore.getInstance().collection("usuarios")
                            .document(usuario.getIdUsuario()).update("correo", correo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) usuario.setCorreo(correo);
                              }
                            });
                        }
                      }
                    });
                  }

                  // Cambio de Contraseña
                  if (nclave.length() > 0) {
                    mAuth.getCurrentUser().updatePassword(nclave);
                  }

                  if (!alias.equals(usuario.getAlias())){
                    FirebaseFirestore.getInstance().collection("usuarios")
                      .document(usuario.getIdUsuario()).update("alias", alias)
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()) usuario.setAlias(alias);
                        }
                      });
                  }

                  View view = getLayoutInflater().inflate(R.layout.dialog_save_user, null, false);
                  AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                  builder.setView(view)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("usuario", usuario);
                        Intent settings = new Intent(SettingActivity.this, ListActivity.class);
                        settings.putExtras(bundle);
                        startActivity(settings);
                      }
                    }).show();


                } else {
                  task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      TextInputLayout tilContrasena = findViewById(R.id.til_contrasena);
                      tilContrasena.setError(getString(R.string.wrong_password));
                      tilContrasena.setErrorEnabled(true);
                      Log.e("SettingActivity", "Contraseña Incorrecta: " + e.toString());
                    }
                  });
                }
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                TextInputLayout tilContrasena = findViewById(R.id.til_contrasena);
                tilContrasena.setError(getString(R.string.wrong_password));
                tilContrasena.setErrorEnabled(true);
                Log.e("SettingActivity", "Contraseña Incorrecta: " + e.toString());
              }
            });
        } else {
          if (pas.length() == 0) {
            TextInputLayout tilContrasena = findViewById(R.id.til_contrasena);
            tilContrasena.setError(getString(R.string.wrong_password));
            tilContrasena.setErrorEnabled(true);
          } else if (npas.length() > 0) {
            TextInputLayout tilNuevaContrasena = findViewById(R.id.til_nueva_contrasena);
            tilNuevaContrasena.setError(getString(R.string.equal_password));
            tilNuevaContrasena.setErrorEnabled(true);
            TextInputLayout tilNuevaContrasenaR = findViewById(R.id.til_nueva_contrasena_r);
            tilNuevaContrasenaR.setError(getString(R.string.equal_password));
            tilNuevaContrasenaR.setErrorEnabled(true);
          }
        }
      }
    });

    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        Intent intent = new Intent(SettingActivity.this, ListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

  }

  private void setupFloatingLabelError() {
    final TextInputLayout tilContrasena = (TextInputLayout) findViewById(R.id.til_contrasena);
    tilContrasena.getEditText().addTextChangedListener(new TextWatcher() {
      // ...
      @Override
      public void onTextChanged(CharSequence text, int start, int count, int after) {

      }
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
        // TODO Auto-generated method stub
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.length() < 6) {
          tilContrasena.setError(getString(R.string.min_password));
          tilContrasena.setErrorEnabled(true);
        } else {
          tilContrasena.setErrorEnabled(false);
        }
      }
    });
    final TextInputLayout tilNuevaContrasena = (TextInputLayout) findViewById(R.id.til_nueva_contrasena);
    tilNuevaContrasena.getEditText().addTextChangedListener(new TextWatcher() {
      // ...
      @Override
      public void onTextChanged(CharSequence text, int start, int count, int after) {

      }
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
        // TODO Auto-generated method stub
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.length() <= 6) {
          tilNuevaContrasena.setError(getString(R.string.min_password));
          tilNuevaContrasena.setErrorEnabled(true);
        } else {
          tilNuevaContrasena.setErrorEnabled(false);
        }
      }
    });
    final TextInputLayout tilNuevaContrasenaR = (TextInputLayout) findViewById(R.id.til_nueva_contrasena_r);
    tilNuevaContrasenaR.getEditText().addTextChangedListener(new TextWatcher() {
      // ...
      @Override
      public void onTextChanged(CharSequence text, int start, int count, int after) {

      }
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
        // TODO Auto-generated method stub
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.length() <= 6) {
          tilNuevaContrasenaR.setError(getString(R.string.min_password));
          tilNuevaContrasenaR.setErrorEnabled(true);
        } else {
          tilNuevaContrasenaR.setErrorEnabled(false);
        }
      }
    });
  }

  private Boolean checkNewPasswordError() {
    if (pas.length() == 0) return false;
    else if (npas.length() > 0) return (npas.getText().toString().equals(npasr.getText().toString()));
    else return true;
  }
}
