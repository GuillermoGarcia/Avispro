package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Combatiente implements Comparable<Combatiente>, Serializable {

  @Expose @SerializedName("acciones") private int acciones;
  @Expose @SerializedName("avatar") private String avatar;
  @Expose @SerializedName("idCombatiente") private String idCombatiente;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("reflejos") private long reflejos;
  @Expose @SerializedName("velocidadArma") private int velocidadArma;

  public Combatiente() {
    reflejos = 0;
    acciones = 0;
    velocidadArma = 0;
  }

  public Combatiente(String avatar, String idCombatiente, String nombre, long reflejos, int velocidadArma) {
    this.avatar = avatar;
    this.idCombatiente = idCombatiente;
    this.nombre = nombre;
    this.reflejos = reflejos;
    this.velocidadArma = velocidadArma;
  }

  public Combatiente(String nombre, long reflejos, int velocidadArma) {
    this.avatar =        "";
    this.nombre =        nombre;
    this.reflejos =      reflejos;
    this.velocidadArma = velocidadArma;
  }

  public Combatiente(String idPersonaje, String nombre, long reflejos) {
    avatar =             "";
    this.idCombatiente = idPersonaje;
    this.nombre =        nombre;
    this.reflejos =      reflejos;
    velocidadArma =      0;
  }

  /**      Getters      **/
  public String getAvatar() { return avatar; }
  public String getIdCombatiente() { return idCombatiente; }
  public String getNombre() { return nombre; }
  public long   getReflejos() { return reflejos; }
  public int    getVelocidadArma() { return velocidadArma; }

  /**      Setters      **/
  public void setAvatar(String avatar) { this.avatar= avatar; }
  public void setIdCombatiente(String idCombatiente) { this.idCombatiente = idCombatiente; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setReflejos(long reflejos) { this.reflejos = reflejos; }
  public void setVelocidadArma(int velocidadArma) { this.velocidadArma = velocidadArma; }


  @Override
  public int compareTo(Combatiente c) {
    long i = this.obtenerIniciativa() - c.obtenerIniciativa();
    long j = this.reflejos - c.getReflejos();
    return (i == 0) ? (j == 0) ? 0 : ((j > 0) ? 1 : -1) : (i > 0) ? 1 : -1;
  }

  /**        Métodos Públicos        **/

  /**
   * Guardar o actualizar el combatiente en Firebase
   */
  public void actualizarCombatiente() {
    FirebaseFirestore.getInstance().collection("combatiente")
        .document(idCombatiente).set(this)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            Log.d("Combatiente", "Combatiente Nuevo Guardado Exitosamente");
          }
        });
  }

  /**
   * Usar una acción del turno
   */
  public void ejecutarAccion() { acciones--; }

  /**
   * Guardar combatiente en firebase
   */
  public void guardarCombatiente() {
    final Combatiente c = this;
    if (c.getIdCombatiente() != null) {
      FirebaseFirestore.getInstance().collection("combatiente").document(c.getIdCombatiente())
        .set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d("Combatiente", "Combatiente Guardado Exitosamente");
        }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d("Combatiente", "Combatiente No Guardado Exitosamente");
          }
        });
    } else {
      FirebaseFirestore.getInstance().collection("combatiente").add(c)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("combatiente")
              .document(id).update("idCombatiente", id);
            c.setIdCombatiente(id);
            Log.d("Combatiente", "Combatiente Nuevo Guardado Exitosamente");
          }
        });
    }
  }

  /**
   * Iniciar las acciones por un turno del combatiente
   */
  public void iniciarAcciones() {
    acciones = (this.reflejos > 35) ? 3 : (this.reflejos > 10) ? 2 : 1;
  }

  /**
   * Iniciativa del Combatiente
   * @return, iniciativa del combatiente
   */
  public long obtenerIniciativa() {return reflejos - velocidadArma; }

  /**
   * Si el combatiente le quedan acciones en un turno devuelve true
   * @return, True si el combatiente le quedan acciones en el turno, false si no
   */
  public boolean tieneAcciones() { return acciones > 0; }

  /**
   * Devuelve si el combatiente tiene o no avatar.
   * @return, True si el combatiente tiene avatar, false si no.
   */
  public boolean tieneAvatar() { return avatar.equals(""); }

}
