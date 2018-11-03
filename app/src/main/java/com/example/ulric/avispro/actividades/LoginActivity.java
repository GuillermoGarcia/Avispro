package com.example.ulric.avispro.actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

  Button btnLogin, btnRegister;
  EditText usu, pas;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);


    btnLogin = findViewById(R.id.boton_login);
    btnRegister = findViewById(R.id.boton_register);

    usu = findViewById(R.id.label_usuario);
    pas = findViewById(R.id.label_contrasena);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null){
      String usuario = bundle.getString("usuario");
      usu.setText(usuario);
    }

    btnRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), RegisterActivity.class);
        startActivity(intent);
      }
    });

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String usr = usu.getText().toString().trim();
        String clv = pas.getText().toString().trim();
        if (usr.isEmpty() || clv.isEmpty()){
          Snackbar.make(v, R.string.empty_login, Snackbar.LENGTH_LONG).show();
        } else {
          // Nos logueamos contra una API o Firebase
          // En este caso usaremos Firebase
          loginWithFirebase(usr, clv);
        }

      }
    });
  }

  protected void loginWithFirebase(String usuario, String clave){
    // Obtenemos instancia de Firebase (Authenticate)
    final FirebaseAuth mAuth = FirebaseAuth.getInstance() ;

    // Loguearnos con Firebase utilizando el correo y la contraseña
    mAuth.signInWithEmailAndPassword(usuario, clave)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {

              // Recuperamos la información de la base de datos
              // de Firebase
              FirebaseDatabase db = FirebaseDatabase.getInstance() ;

              // Creamos una referencia al documento USUARIOS
              DatabaseReference ref = db.getReference("usuario") ;

              // Obtenemos la información del usuario
              ref.child(mAuth.getCurrentUser().getUid())
                  .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                      if (dataSnapshot.exists()) {

                        // Rescatamos la información devuelta por Firebase
                        Usuario usuario = dataSnapshot.getValue(Usuario.class) ;

                        // Creamos la intención
                        Intent intent = new Intent(LoginActivity.this, ListActivity.class) ;

                        // Creamos un objeto de tipo Bundle
                        Bundle bundle = new Bundle() ;
                        bundle.putSerializable("usuario", usuario) ;

                        // Asociamos el Bundle al Intent
                        intent.putExtras(bundle) ;

                        // Lanzar la actividad ListActivity
                        startActivity(intent) ;
                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                  });
              }
            }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, R.string.wrong_login, Toast.LENGTH_LONG).show();
              }
          });

  }
}
