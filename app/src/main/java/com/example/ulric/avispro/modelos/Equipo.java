package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Equipo {

  @Expose @SerializedName("idEquipo") private String idEquipo;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("peso") private int peso;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("cantidadDeUsosMaxima") private int cantidadDeUsosMaxima;
  @Expose @SerializedName("esArma") private boolean esArma;


  public Equipo() {}

  public Equipo(String idEquipo, String nombre, int peso, String descripcion,
                int cantidadDeUsosMaxima, boolean esArma) {
    this.idEquipo = idEquipo;
    this.nombre = nombre;
    this.peso = peso;
    this.descripcion = descripcion;
    this.cantidadDeUsosMaxima = cantidadDeUsosMaxima;
    this.esArma = esArma;
  }

  public int     getCantidadDeUsosMaxima() { return cantidadDeUsosMaxima; }
  public String  getDescripcion() { return descripcion; }
  public String  getIdEquipo() { return idEquipo; }
  public String  getNombre() { return nombre; }
  public int     getPeso() { return peso; }
  public boolean isArma() { return esArma; }

  public void setCantidadDeUsosMaxima(int cantidadDeUsosMaxima) { this.cantidadDeUsosMaxima = cantidadDeUsosMaxima; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setEsArma(boolean esArma) { this.esArma = esArma; }
  public void setIdEquipo(String idEquipo) { this.idEquipo = idEquipo; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setPeso(int peso) { this.peso = peso; }


  public void guardarEquipo() {
    if (idEquipo != null) {
      FirebaseFirestore.getInstance().collection("equipo")
        .document(idEquipo).set(this).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d("Equipo", "Equipo Guardado Exitosamente");
         }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d("Equipo", "Equipo No Guardado Exitosamente");
          }
        });
    } else {
      FirebaseFirestore.getInstance().collection("equipo").add(this)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("equipo")
              .document(id).update("idEquipo", id);
            idEquipo = id;
            Log.d("Equipo", "Equipo Nuevo Guardado Exitosamente");
          }
        });
    }
  }



}
