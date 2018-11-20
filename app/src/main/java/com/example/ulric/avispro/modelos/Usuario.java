package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

  public Usuario (){}

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


  public List<Personaje> cargarPersonajes(){
    final List<Personaje> personajes = new ArrayList<>();

    List<String> idPersonajes = this.getPersonajes();
    final Semaphore semaphores = new Semaphore(idPersonajes.size());
    semaphores.acquireUninterruptibly(idPersonajes.size());
    Log.d("Get Personajes", "Semaforo Cerrado. Inicio For de Ids de Personajes.");
    for (String id : idPersonajes) {
      FirebaseFirestore.getInstance().collection("personajes").document(id).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              // Leemos el documento seleccionado
              DocumentSnapshot doc = task.getResult();

              if (doc.exists()) {
                personajes.add(doc.toObject(Personaje.class));
                semaphores.release();
              }
            }
          }
        });
    }
    Log.d("Get Personajes", "Terminado For de Ids de Personajes.");
    semaphores.acquireUninterruptibly(idPersonajes.size());
    Log.d("Get Personajes", "Semaforo abierto.");
    return personajes;
  }

}
