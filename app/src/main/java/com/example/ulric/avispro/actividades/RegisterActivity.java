package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

  private Button btnSignUp, btnCancel;
  private EditText ema, pas, ali;

  private FirebaseAuth mAuth;
  private FirebaseDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    btnSignUp = findViewById(R.id.boton_accept);
    btnCancel = findViewById(R.id.boton_cancel);

    ema = findViewById(R.id.label_usuario);
    pas = findViewById(R.id.label_contrasena);
    ali = findViewById(R.id.label_alias);

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // Obtenemos los valores para el acceso del usuario
        String correo = ema.getText().toString().trim();
        String clave = pas.getText().toString().trim();

        // Obtenemos una instancia de Firebase
        mAuth = FirebaseAuth.getInstance();
        // Creamos el usuario nuevo
        mAuth.createUserWithEmailAndPassword(correo, clave)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()){
              // Obtenemos el uid del nuevo usuario
              String uid = mAuth.getCurrentUser().getUid();

              // Obtenemos una instancia de la BD de Firebase
              db = FirebaseDatabase.getInstance();
              // Obtenemos una referencia al documento 'usuario'
              DatabaseReference ref = db.getReference("usuario");

              // Guardamos los datos del nuevo usuario en el documento
              ref.child(uid).setValue(new Usuario(uid, ema.getText().toString().trim(),
                  ali.getText().toString().trim()));

              // Creamos la intención de cambiar de actividad y lo hacemos
              Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
              intent.putExtra("usuario",ema.getText().toString().trim());
              startActivity(intent);
            } else {
              task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.e("FALLO REGISTRO",e.toString());
                }
              });
            }
          }
        });
      }
    });

    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

  }
}
