package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
  public void guardarPersonajes(List<String> personajes) { this.personajes = personajes; }


  /**      Métodos Públicos      **/

  /**
   * Borrar personaje del usuario
   * @param id, identificador del personaje a borrar
   */
  public void borrarPersonaje(String id) {
    this.personajes.remove(id);
    this.actualizarListaPersonaje();
  }

  /**
   * Guardar identificador de un nuevo personaje del usuario
   * @param id, identidicador del nuevo personaje del usuario
   */
  public void guardarPersonaje(String id){
    this.personajes.add(id);
    actualizarListaPersonaje();
  }

  /**
   * Dejar de estar identificado en Firebase y en la aplicación
   */
  public void salir(){ FirebaseAuth.getInstance().signOut(); }


  /**      Métodos Privados      **/

  /**
   * Actualizar en Firebase la lista de personajes del usuario
   */
  private void actualizarListaPersonaje(){
    FirebaseFirestore.getInstance().collection("usuarios")
      .document(idUsuario).update("personajes", this.personajes)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d("UsuarioClass", "Lista de Personajes Actualizada.");
          }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("UsuarioClass", "Error en Actualización Lista de Personajes.", e);
        }
      });
  }

}

