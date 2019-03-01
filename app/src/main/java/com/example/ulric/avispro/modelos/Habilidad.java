package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Habilidad implements Serializable, Comparable<Habilidad> {

  @Expose @SerializedName("idHabilidad") private String idHabilidad;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("bonusPrincipal") private ArrayList<String> bonusPrincipal;
  @Expose @SerializedName("bonusSecundario") private ArrayList<String> bonusSecundario;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("tipo") private int tipo;
  @Expose @SerializedName("combate") private boolean combate;
  @Expose @SerializedName("idHabilidadPersonaje") private String idHabilidadPersonaje;
  @Expose @SerializedName("extra") private int extra;
  @Expose @SerializedName("habilidadUsada") private boolean habilidadUsada;
  @Expose @SerializedName("pap") private int pap;
  @Expose @SerializedName("valorBase") private int valorBase;
  private int valorBonusPrincipal =  -10;
  private int valorBonusSecundario = -4;
  private String bonusPrincipalUsado;
  private String bonusSecundarioUsado;

  public Habilidad() {}

  public Habilidad(String idHabilidad, String nombre, ArrayList<String> bonusPrincipal,
                   ArrayList<String> bonusSecundario, String descripcion, int tipo, boolean combate) {
    this.bonusPrincipal =  bonusPrincipal;
    this.bonusSecundario = bonusSecundario;
    this.combate =         combate;
    this.descripcion =     descripcion;
    this.idHabilidad =     idHabilidad;
    this.nombre =          nombre;
    this.tipo =            tipo;
  }

  public Habilidad(int extra, boolean habilidadUsada, String idHabilidad, String idHabilidadPersonaje, int pap, int valorBase) {
    this.extra =                extra;
    this.habilidadUsada =       habilidadUsada;
    this.idHabilidad =          idHabilidad;
    this.idHabilidadPersonaje = idHabilidadPersonaje;
    this.pap =                  pap;
    this.valorBase =            valorBase;
  }

  public ArrayList<String> getBonusPrincipal() { return bonusPrincipal; }
  public ArrayList<String> getBonusSecundario() { return bonusSecundario; }
  public String getDescripcion() { return descripcion; }
  public String getIdHabilidad() { return idHabilidad; }
  public String getNombre() { return nombre; }
  public int getTipo() { return tipo; }
  public boolean isCombate() { return combate; }
  public String getIdHabilidadPersonaje() { return idHabilidadPersonaje; }
  public int getExtra() { return this.extra; }
  public boolean getHabilidadUsada() { return this.habilidadUsada; }
  public int getPap() { return this.pap; }
  public int getValorBase() { return this.valorBase; }

  public void setBonusPrincipal(ArrayList<String> bonusPrincipal) { this.bonusPrincipal = bonusPrincipal; }
  public void setBonusSecundario(ArrayList<String> bonusSecundario) { this.bonusSecundario = bonusSecundario; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setIdHabilidad(String idHabilidad) { this.idHabilidad = idHabilidad; }
  public void setCombate(boolean combate) { this.combate = combate; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setTipo(int tipo) { this.tipo = tipo; }
  public void setIdHabilidadPersonaje(String idHabilidadPersonaje) { this.idHabilidadPersonaje = idHabilidadPersonaje; }
  public void setExtra(int extra) { this.extra = extra; }
  public void guardarHabilidadUsada(boolean habilidadUsada) { this.habilidadUsada = habilidadUsada; }
  public void setPap(int pap) { this.pap = pap; }
  public void setValorBase(int valorBase) { this.valorBase = valorBase; }

  @Override
  public int compareTo(Habilidad h) {
    return this.nombre.compareTo(h.getNombre());
  }


  /**        Métodos Públicos        **/

  /**
   * Borrar de Firebase la habilidad
   */
  public void borrarHabilidad() {
    FirebaseFirestore.getInstance().collection("habilidadPersonaje")
        .document(this.idHabilidadPersonaje).delete();
  }

  /**
   * Calcula los bonus aplicables a la habilidad en función del personaje
   * @param p, personaje vinculado a la habilidad
   */
  public void calcularBonusHabilidad(Personaje p) {
    if ((this.bonusPrincipal != null) && (this.bonusPrincipal.size() > 0)) {
      for (String bonus : this.bonusPrincipal) {
        if (this.valorBonusPrincipal < p.valorBonusPrincipal(bonus)) {
          this.valorBonusPrincipal = p.valorBonusPrincipal(bonus);
          this.bonusPrincipalUsado = bonus;
        }
      }
    } else if (this.bonusPrincipal.size() == 0) {
      this.valorBonusPrincipal = 0;
    }
    this.bonusSecundario.addAll(this.bonusPrincipal);
    this.bonusSecundario.remove(this.bonusPrincipalUsado);
    if ((this.bonusSecundario != null) && (this.bonusSecundario.size() > 0)) {
      for (String bonus : this.bonusSecundario) {
        if (this.valorBonusSecundario < p.valorBonusSecundario(bonus)) {
          this.valorBonusSecundario = p.valorBonusSecundario(bonus);
          this.bonusSecundarioUsado = bonus;
        }
      }
    } else if (this.bonusSecundario.size() == 0) {
      this.valorBonusSecundario = 0;
    }
  }

  /**
   * Completamos una habilidad con datos base con los datos particulares
   * @param hab, habilidad con los datos particulares
   */
  public void completarHabilidad(Habilidad hab) {
    this.setExtra(hab.getExtra());
    this.guardarHabilidadUsada(hab.getHabilidadUsada());
    this.setIdHabilidadPersonaje(hab.getIdHabilidadPersonaje());
    this.setPap(hab.getPap());
    this.setValorBase(hab.getValorBase());
  }

  /**
   * Guardamos en Firebase los datos particulares de la habilidad, si ya existe los actualiza
   * @param mch, función de retorno de los datos
   */
  public void guardarHabilidad(final MyCallbackData mch) {
    Map<String, Object> data = new HashMap<>();
    data.put("extra", this.getExtra());
    data.put("habilidadUsada", this.getHabilidadUsada());
    data.put("idHabilidad", this.getIdHabilidad());
    data.put("idHabilidadPersonaje", this.getIdHabilidadPersonaje());
    data.put("pap", this.getPap());
    data.put("valorBase", this.getValorBase());
    if (this.idHabilidadPersonaje == null) {
      FirebaseFirestore.getInstance().collection("habilidadPersonaje").add(data)
          .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
              String id = documentReference.getId();
              FirebaseFirestore.getInstance().collection("habilidadPersonaje")
                  .document(id).update("idHabilidadPersonaje", id);
              mch.onCallbackData(id);
            }
          });
    } else {
      FirebaseFirestore.getInstance().collection("habilidadPersonaje")
          .document(this.idHabilidadPersonaje).update(data);
    }
  }

  /**
   * Carga los datos de la habilidad desde Firebase
   * @param hab, habilidad a cargar los datos
   */
  public void obtenerHabilidad(final Habilidad hab) {
    FirebaseFirestore.getInstance().collection("habilidad")
      .document(hab.getIdHabilidad()).get()
      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
              Log.d("Habilidad", "DocumentSnapshot data: " + document.getData());
              hab.completarHabilidad(document.toObject(Habilidad.class));
            } else {
              Log.d("Habilidad", "No such document");
            }
          } else {
            Log.d("Habilidad", "get failed with ", task.getException());
          }
        }
      });
  }

  /**
   * Carga desde Firebase los datos base de la habilidad
   * @param mch, función de retorno para los datos
   */
  public void obtenerHabilidadGeneral(final MyCallbackData mch) {
    FirebaseFirestore.getInstance().collection("habilidad")
      .document(this.getIdHabilidad()).get()
      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
              Log.d("Habilidad", "DocumentSnapshot data: " + document.getData());
              mch.onCallbackData(document.toObject(Habilidad.class));
            } else {
              Log.d("Habilidad", "No such document");
            }
          } else {
            Log.d("Habilidad", "get failed with ", task.getException());
          }
        }
      });
  }

  /**
   * Carga desde Firebase los datos particulares de la habilidad identificada por idHabilidad
   * @param idHabilidad, identificador de la habilidad
   * @param mch, función de retorno de los datos
   */
  public static void obtenerHabilidadPersonaje(String idHabilidad, final MyCallbackData mch) {
    FirebaseFirestore.getInstance().collection("habilidadPersonaje")
        .document(idHabilidad).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                Log.d("Habilidad", "DocumentSnapshot data: " + document.getData());
                mch.onCallbackData(document.toObject(Habilidad.class));
              } else {
                Log.d("Habilidad", "No such document");
              }
            } else {
              Log.d("Habilidad", "get failed with ", task.getException());
            }
          }
        });
  }

  /**
   * Devuelve el valor, como cadena, del bonus principal
   * @return String, cadena con el valor del bonus principal
   */
  public String obtenerValorBonusPrincipal(){
    return this.valorBonusPrincipal + "";
  }

  /**
   * Devuelve el valor, como cadena, del bonus secundario
   * @return String, cadena con el valor del bonus secundario
   */
  public String obtenerValorBonusSecundario(){
    return this.valorBonusSecundario + "";
  }

  /**
   * Devuelve el valor, como cadena, de la suma del valor base y los bonus principal y secundario
   * @return String, cadena con la suma del valor base y los bonus principal y secundario
   */
  public String obtenerValorTotal(){
    return this.valorBase + this.valorBonusPrincipal + this.valorBonusSecundario + "";
  }

}
