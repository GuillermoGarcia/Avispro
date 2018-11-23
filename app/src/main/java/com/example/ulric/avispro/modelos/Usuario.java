package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ulric.avispro.interfaces.MyCallbackPersonaje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Usuario implements Serializable {
    @Expose @SerializedName("idUsuario") private String idUsuario;
    @Expose @SerializedName("correo") private String correo;
    @Expose @SerializedName("alias") private String alias;
    @Expose @SerializedName("avatar") private String avatar;
    @Expose @SerializedName("personajes") private List<String> personajes;

  public Usuario (){ }

  public Usuario(String idUsuario, String correo, String alias, List<String> personajes) {
    this.idUsuario = idUsuario;
    this.correo = correo;
    this.alias = alias;
    this.personajes = personajes;
  }


  public String getAlias() { return alias; }
  public String getAvatar() { return avatar; }
  public String getCorreo() { return correo; }
  public String getIdUsuario() { return idUsuario; }
  public List<String> getPersonajes() { return personajes; }
  public void setAlias(String alias) { this.alias = alias; }
  public void setAvatar(String avatar) { this.avatar = avatar; }
  public void setCorreo (String correo) { this.correo = correo; }
  public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
  public void setPersonajes(List<String> personajes) { this.personajes = personajes; }



  public void cargarPersonajes(final MyCallbackPersonaje myCallbackPersonaje){

    List<String> idPersonajes = this.getPersonajes();
    for (final String id : idPersonajes) {
      FirebaseFirestore.getInstance().collection("personajes").document(id).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("Usuario", "DocumentSnapshot data: " + document.getData());
                myCallbackPersonaje.onCallback(document.toObject(Personaje.class));
              } else {
                Log.d("Usuario", "No such document");
              }
            } else {
              Log.d("Usuario", "get failed with ", task.getException());
            }
          }
        });
    }

  }

}

