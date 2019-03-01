package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EquipoPersonaje extends Equipo {

  @Expose @SerializedName("idEquipoPersonaje") private String idEquipoPersonaje;
  @Expose @SerializedName("idEquipo") private String idEquipo;
  @Expose @SerializedName("cantidad") private int cantidad;
  @Expose @SerializedName("cantidadDeUsos") private int cantidadDeUsos;
  @Expose @SerializedName("equipado") private boolean equipado;
  @Expose @SerializedName("transportado") private boolean transportado;
  @Expose @SerializedName("prestado") private boolean prestado;
  @Expose @SerializedName("prestadoA") private String prestadoA;

  public EquipoPersonaje() {
    this.equipado = false;
    this.transportado = true;
    this.prestado = false;
    this.prestadoA = "";
  }

  public EquipoPersonaje(String idEquipoPersonaje, String idEquipo, int cantidad, int cantidadDeUsos) {
    this.idEquipoPersonaje = idEquipoPersonaje;
    this.idEquipo = idEquipo;
    this.cantidad = cantidad;
    this.cantidadDeUsos = cantidadDeUsos;
    this.equipado = false;
    this.transportado = true;
    this.prestado = false;
    this.prestadoA = "";
  }

  public EquipoPersonaje(String idEquipoPersonaje, String idEquipo, int cantidad, int cantidadDeUsos,
                         boolean equipado, boolean transportado, boolean prestado, String prestadoA) {
    this.idEquipoPersonaje = idEquipoPersonaje;
    this.idEquipo = idEquipo;
    this.cantidad = cantidad;
    this.cantidadDeUsos = cantidadDeUsos;
    this.equipado = equipado;
    this.transportado = transportado;
    this.prestado = prestado;
    this.prestadoA = prestadoA;
  }

  public String  getIdEquipoPersonaje() { return idEquipoPersonaje; }
  public String  getIdEquipo() { return idEquipo; }
  public int     getCantidad() { return cantidad; }
  public int     getCantidadDeUsos() { return cantidadDeUsos; }
  public boolean isEquipado() { return equipado; }
  public boolean isTransportado() { return transportado; }
  public boolean isPrestado() { return prestado; }
  public String  getPrestadoA() { return prestadoA; }

  public void setIdEquipoPersonaje(String idEquipoPersonaje) { this.idEquipoPersonaje = idEquipoPersonaje; }
  public void setIdEquipo(String idEquipo) { this.idEquipo = idEquipo; }
  public void setCantidad(int cantidad) { this.cantidad = cantidad; }
  public void setCantidadDeUsos(int cantidadDeUsos) { this.cantidadDeUsos = cantidadDeUsos; }
  public void setEquipado(boolean equipado) { this.equipado = equipado; }
  public void setTransportado(boolean transportado) { this.transportado = transportado; }
  public void setPrestado(boolean prestado) { this.prestado = prestado; }
  public void setPrestadoA(String prestadoA) { this.prestadoA = prestadoA; }

  public void guardarEquipoPersonaje() {
    EquipoPersonaje ep = new EquipoPersonaje(idEquipoPersonaje, idEquipo, cantidad, cantidadDeUsos,
        equipado, transportado, prestado, prestadoA);
    if (idEquipoPersonaje != null) {
      FirebaseFirestore.getInstance().collection("equipoPersonaje")
        .document(idEquipoPersonaje).set(ep).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d("EquipoPersonaje", "Equipo Guardado Exitosamente");
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d("EquipoPersonaje", "Equipo No Guardado Exitosamente");
          }
        });
    } else {
      FirebaseFirestore.getInstance().collection("equipoPersonaje").add(ep)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("equipoPersonaje")
              .document(id).update("idEquipoPersonaje", id);
            idEquipoPersonaje = id;
            Log.d("EquipoPersonaje", "Equipo Nuevo Guardado Exitosamente");
          }
        });
    }
  }

  /**
   * Prestar el objeto a otra persona.
   * @param nombre, a quien se le presta el objeto
   */
  public void prestarA(String nombre) {
    prestado = true;
    prestadoA = nombre;
    equipado = false;
    transportado = false;
  }

  /**
   * Si el equipo le quedan usos devuelve true
   * @return boolean, true si le quedan usos al equipo
   */
  public boolean tieneUsos() { return (cantidadDeUsos > 0); }

  /**
   * Gastar un uso del equipo
   */
  public void usar() { cantidadDeUsos--; }

}