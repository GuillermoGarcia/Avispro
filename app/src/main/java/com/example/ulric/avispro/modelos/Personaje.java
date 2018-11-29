package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Personaje implements Serializable, Comparable {

  @Expose @SerializedName("avatar") private String avatar;
  @Expose @SerializedName("caracteristicas") private Map<String, ArrayList<Long>> caracteristicas;
  @Expose @SerializedName("cultura") private String cultura;
  @Expose @SerializedName("edad") private int edad;
  @Expose @SerializedName("idPersonaje") private String idPersonaje;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("nivel") private int nivel;
  @Expose @SerializedName("procedencia") private String procedencia;
  @Expose @SerializedName("raza") private String raza;

  public Personaje() {
    this.avatar = "";
    final ArrayList<Long> min = new ArrayList<Long>() {{ add(0,Long.valueOf("4")); add(1,Long.valueOf("1")); }};
    this.caracteristicas = new HashMap<String, ArrayList<Long>>() {{
      put("Agi", min); put("Apa", min); put("Con",  min); put("Des", min);
      put("Emp", min); put("For", min); put("Inte", min); put("Mem", min);
      put("Ref", min); put("Per", min); put("Pod",  min); put("Vol", min);
    }};
  }
  public Personaje(String avatar, Map<String, ArrayList<Long>> caracteristicas, String cultura, int edad, String idPersonaje, String nombre,
                   int nivel, String procedencia, String raza) {
    this.avatar = avatar;
    this.caracteristicas = caracteristicas;
    this.cultura = cultura;
    this.edad = edad;
    this.idPersonaje = idPersonaje;
    this.nombre = nombre;
    this.nivel = nivel;
    this.procedencia = procedencia;
    this.raza = raza;
  }

  public String getAvatar() { return avatar; }
  public Map<String, ArrayList<Long>> getCaracteristicas() { return caracteristicas; }
  public String getCultura() { return cultura; }
  public int getEdad() { return edad; }
  public String getIdPersonaje() { return idPersonaje; }
  public String getNombre() { return nombre; }
  public int getNivel() { return nivel; }
  public String getProcedencia() { return procedencia; }
  public String getRaza() { return raza; }

  public void setAvatar(String avatar) { this.avatar = avatar; }
  public void setCaracteristicas(Map<String, ArrayList<Long>> caracteristicas) { this.caracteristicas = caracteristicas; }
  public void setCultura(String cultura) { this.cultura = cultura; }
  public void setEdad(int edad) { this.edad = edad; }
  public void setIdPersonaje(String idPersonaje) { this.idPersonaje = idPersonaje; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setNivel(int nivel) { this.nivel = nivel; }
  public void setProcedencia(String procedencia) { this.procedencia = procedencia; }
  public void setRaza(String raza) { this.raza = raza; }


  public void setPersonaje(final Usuario usuario, final MyCallbackData mcp) {
    if (this.idPersonaje != null) {
      FirebaseFirestore.getInstance().collection("personajes").document(this.idPersonaje)
        .set(this).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("Personaje", "Personaje Guardado Exitosamente");
          mcp.onCallbackData(new Personaje());
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("Personaje", "Personaje No Guardado Exitosamente");
          mcp.onCallbackData(new Personaje());
        }
      });
    } else {
      FirebaseFirestore.getInstance().collection("personajes").add(this)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("personajes")
              .document(id).update("idPersonaje", id);
            usuario.setPersonaje(id);
            Log.d("Personaje", "Personaje Nuevo Guardado Exitosamente");
            mcp.onCallbackData(new Personaje());
          }
        });
    }
  }

  public void deletePersonaje(){
    FirebaseFirestore.getInstance().collection("personajes")
      .document(this.idPersonaje).delete();
  }

  @Override
  public int compareTo(Object o) {
    if (o.getClass() == this.getClass()) {
      Personaje p = (Personaje) o;
      return this.nombre.compareTo(p.getNombre());
    } else {
      return -1;
    }
  }
}
