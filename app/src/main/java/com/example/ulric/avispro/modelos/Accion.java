package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Accion {

  @Expose @SerializedName("idAccion") private String idAccion;
  @Expose @SerializedName("turno") private int turno;
  @Expose @SerializedName("iniciativa") private int iniciativa;
  @Expose @SerializedName("atacante") private String atacante;
  @Expose @SerializedName("defensor") private String defensor;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("exito") private boolean exito;

  public Accion() {}

  public Accion(String idAccion, int turno, int iniciativa, String atacante, String defensor,
                String descripcion, boolean exito) {
    this.idAccion = idAccion;
    this.turno = turno;
    this.iniciativa = iniciativa;
    this.atacante = atacante;
    this.defensor = defensor;
    this.descripcion = descripcion;
    this.exito = exito;
  }

  public String getIdAccion() { return idAccion; }
  public int getTurno() { return turno; }
  public int getIniciativa() { return iniciativa; }
  public String getAtacante() { return atacante; }
  public String getDefensor() { return defensor; }
  public String getDescripcion() { return descripcion; }
  public boolean isExito() { return exito; }

  public void setIdAccion(String idAccion) { this.idAccion = idAccion; }
  public void setTurno(int turno) { this.turno = turno; }
  public void setIniciativa(int iniciativa) { this.iniciativa = iniciativa; }
  public void setAtacante(String atacante) { this.atacante = atacante; }
  public void setDefensor(String defensor) { this.defensor = defensor; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setExito(boolean exito) { this.exito = exito; }


  /**
   * Guardar la acción en Firebase
   */
  public void guardarAccion() {
    if (idAccion != null) {
      FirebaseFirestore.getInstance().collection("accion").document(idAccion)
        .set(this).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d("Acción", "Acción Guardada Exitosamente");
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d("Acción", "Acción No Guardada Exitosamente");
          }
        });
    } else {
      FirebaseFirestore.getInstance().collection("accion").add(this)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("accion")
              .document(id).update("idCombate", id);
            idAccion = id;
            Log.d("Acción", "Nueva Acción Guardada Exitosamente");
          }
        });
    }

  }

}
