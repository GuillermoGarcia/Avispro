package com.example.ulric.avispro.modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;

import javax.annotation.Nullable;

public class Combate implements Serializable {

  @Expose @SerializedName("idCombate") private String idCombate;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("turno") private int turno;
  @Expose @SerializedName("master") private String master;
  @Expose @SerializedName("idMaster") private String idMaster;
  @Expose @SerializedName("idPjs") private List<String> idPjs;
  @Expose @SerializedName("pjs") private List<Combatiente> pjs;
  @Expose @SerializedName("idPnjs") private List<String> idPnjs;
  @Expose @SerializedName("pnjs") private List<Combatiente> pnjs;
  @Expose @SerializedName("iniciativa") private int iniciativa;
  @Expose @SerializedName("idAcciones") private List<String> idAcciones;
  @Expose @SerializedName("acciones") private List<Accion> acciones;
  @Expose @SerializedName("orden") private List<List<String>> orden;

  public Combate() {
    orden = new ArrayList<>(41);
    for (int i = 1; i <= orden.size(); i++) {
      orden.add(i, new ArrayList<String>(0));
    }
    pjs = new ArrayList<>();
    pnjs = new ArrayList<>();
    turno = 0;
    iniciativa = 40;
  }

  public Combate(String idCombate, String nombre, String descripcion, String master, String idMaster,
                 int turno, int iniciativa, List<List<String>> orden, List<String> idPjs,
                 List<String> idPnjs, List<String> idAcciones) {
    this.idCombate =   idCombate;
    this.nombre =      nombre;
    this.descripcion = descripcion;
    this.master =      master;
    this.idMaster =    idMaster;
    this.turno =       turno;
    this.idPjs =       idPjs;
    this.idPnjs =      idPnjs;
    this.iniciativa =  iniciativa;
    this.idAcciones =  idAcciones;
    this.orden =       orden;
    this.pjs =         new ArrayList<>();
    this.pnjs =        new ArrayList<>();
    this.acciones =    new ArrayList<>();
  }


  /**      GETTERS       */
  public String getIdCombate() { return idCombate; }
  public String getNombre() { return nombre; }
  public String getDescripcion() { return descripcion; }
  public String getMaster() { return master; }
  public String getIdMaster() { return idMaster; }
  public int getTurno() { return turno; }
  public List<String> getIdPjs() { return idPjs; }
  public List<String> getIdPnjs() { return idPnjs; }
  public int getIniciativa() { return iniciativa; }
  public List<String> getIdAcciones() { return idAcciones; }
  public List<List<String>> getOrden() { return orden; }
  public List<Combatiente> getPjs() { return pjs; }
  public List<Accion> getAcciones() { return acciones; }

  /**      SETTERS       */
  public void setIdCombate(String idCombate) { this.idCombate = idCombate; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setMaster(String master) { this.master= master; }
  public void setIdMaster(String idMaster) { this.idMaster= idMaster; }
  public void setTurno(int turno) { this.turno = turno; }
  public void setIdPjs(List<String> idPjs) { this.idPjs = idPjs; }
  public void setIdPnjs(List<String> idPnjs) { this.idPnjs = idPnjs; }
  public void setIniciativa(int iniciativa) { this.iniciativa = iniciativa; }
  public void setIdAcciones(List<String> idAcciones) { this.idAcciones = idAcciones; }
  public void setOrden(List<List<String>> orden) { this.orden = orden; }


  /**      Métodos Públicos      **/

  /**
   * Actualizar los identificadores de combatientes en Firebase
   */
  public void actualizarIdPjs() {
    FirebaseFirestore.getInstance().collection("combate")
        .document(this.idCombate).update("idPjs", this.idPjs);
  }

  /**
   * Añadir un combatiente, personaje no jugador, al combate
   * @param pnj, combatiente a añadir al combate
   */
  public void anadirPnj(Combatiente pnj) { this.pnjs.add(pnj); this.idPnjs.add(pnj.getIdCombatiente()); }

  /**
   * Dejar un combatiente, personaje jugador, al combate
   * @param pj, combatiente que va a dejar el combate
   */
  public void dejarCombate(Combatiente pj) { this.idPjs.remove(pj.getIdCombatiente()); this.pjs.remove(pj); }

  /**
   * Si el combate está ya en marcha
   * @return True si el combate está en marcha, False si no
   */
  public boolean enMarcha() { return (this.turno > 0); }

  /**
   * Si el usuario con el id pasado es el Master, director de juego, del combate
   * @param id, identficador del usuario
   * @return True si el usuario es el master del combate
   */
  public boolean esMaster(String id) { return id.equals(this.idMaster); }

  /**
   * Comprobación de si el combatiente es un personaje jugador o un personaje no jugador.
   * @return
   */
  public boolean esPj(String id) { return (this.idPjs.indexOf(id) >= 0); }

  /**
   * Si el combatiente, personaje jugador, está ya unido al combate o no
   * @param id, identificador del combatiente a comprobar
   * @return, True si el combatiente está unido al combate, false si no
   */
  public boolean estaUnido(String id) { return (this.idPjs.indexOf(id) >= 0); }

  /**
   * Guardar en Firebase el combate
   */
  public void guardarCombate() {
    final Combate c = new Combate(this.getIdCombate(), this.getNombre(), this.getDescripcion(),
        this.getMaster(), this.getIdMaster(), this.getTurno(), this.getIniciativa(), this.getOrden(),
        this.getIdPjs(), this.getIdPnjs(), this.getIdAcciones());
    if (c.getIdCombate() != null) {
      FirebaseFirestore.getInstance().collection("combate").document(c.getIdCombate())
          .set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("Combate", "Combate Guardado Exitosamente");
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("Combate", "Combate No Guardado Exitosamente");
        }
      });
    } else {
      FirebaseFirestore.getInstance().collection("combate").add(c)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("combate")
                .document(id).update("idCombate", id);
            c.setIdCombate(id);
            Log.d("Combate", "Combate Nuevo Guardado Exitosamente");
          }
        });
    }
  }

  /**
   * Iniciamos turno de combate, reseteamos el orden
   */
  public void iniciarTurno() {
    this.orden.clear();
    int fin = (pjs.size() >= pnjs.size()) ? pjs.size() : pnjs.size();
    for (int i = 0; i < fin; i++) {
      if (i < pjs.size()) {
        pjs.get(i).iniciarAcciones();
        this.orden.get((int)pjs.get(i).obtenerIniciativa()).add(pjs.get(i).getIdCombatiente());
      }
      if (i < pnjs.size()) {
        pnjs.get(i).iniciarAcciones();
        this.orden.get((int)pnjs.get(i).obtenerIniciativa()).add(pnjs.get(i).getIdCombatiente());
      }
    }
    turno += 1;
    ordenarIniciativa();
    iniciativa = 40;
  }

  /**
   * Devuelve una lista con todos los combatientes del combate
   * @return, lista de combatientes
   */
  public List<Combatiente> listadoCombatientes(){
    List<Combatiente> c = this.pjs;
    c.addAll(this.pnjs);
    Collections.sort(c, new Comparator<Combatiente>() {
      @Override
      public int compare(Combatiente c1, Combatiente c2) {
        return c1.getNombre().compareTo(c2.getNombre());
      }
    });
    return c;
  }

  /**
   * Combatiente/s que actua/n en la iniciativa dada
   * @param i, iniciativa solicitada
   * @return, combatiente/s que actua/n en dicha iniciativa
   */
  public List<String> ordenIniciativa(int i) { return this.orden.get(i); }

  /**
   * El combatiente deja este combate.
   * @param pj, Combatiente que deja este combate
   */
  public void quitarseDelCombate(Combatiente pj) {
    this.pjs.remove(pj);
    this.idPjs.remove(pj.getIdCombatiente());
    this.actualizarIdPjs();
  }

  /**
   * Eliminar combatiente, personaje no jugador, del combate
   * @param pnj, combatiente a quitar del combate
   */
  public void quitarPnj(Combatiente pnj) {
    this.idPnjs.remove(pnj.getIdCombatiente());
    this.pnjs.remove(pnj);
  }

  /**
   * Añadimos la acción realizada por el pj/pnj a la lista de acciones
   * @param a, acción realizada por el pj/pnj
   */
  public void realizarAccion(Accion a, int iniciativa) {
    Combatiente c;
    int id;
    if (idPjs.indexOf(orden.get(iniciativa).get(0)) < 0) {
      id = idPnjs.indexOf(orden.get(iniciativa).get(0));
      c = pnjs.get(id);
    } else {
      id = idPjs.indexOf(orden.get(iniciativa).get(0));
      c = pjs.get(id);
    }
    orden.get(iniciativa).remove(0);
    idAcciones.add(a.getIdAccion());
    c.ejecutarAccion();
    if (c.tieneAcciones()) {
      orden.get(iniciativa - 10).add(c.getIdCombatiente());
    }
  }

  /**
   * Devolvemos al combatiente que le toca actuar
   * @return Combatiente que tiene el turno
   */
  public Combatiente turnoDe() {
    while (this.orden.get(iniciativa).size() < 1) { iniciativa--; }
    Combatiente c;
    int id;
    if (idPjs.indexOf(orden.get(iniciativa).get(0)) < 0) {
      id = idPnjs.indexOf(orden.get(iniciativa).get(0));
      c = pnjs.get(id);
    } else {
      id = idPjs.indexOf(orden.get(iniciativa).get(0));
      c = pjs.get(id);
    }
    return c;
  }

  /**
   * Unirse un combatiente, personaje jugador, al combate
   * @param pj, combatiente a unirse al combate
   */
  public void unirseAlCombate(Combatiente pj) {
    this.pjs.add(pj);
    this.idPjs.add(pj.getIdCombatiente());
    this.actualizarIdPjs();
  }

  /**      Métodos Públicos      **/

  /**
   * Ordenamos a los combatientes de una misma iniciativa.
   */
  private void ordenarIniciativa(){
    for (int i = 40; i > 0; i-- ) {
      if (this.orden.size() > 1) {
        String[] c = (String[]) this.orden.get(i).toArray();
        Arrays.sort(c, new Comparator<String>() {
          @Override
          public int compare(String s1, String s2) {
            int id1 = (idPjs.indexOf(s1) < 0) ? idPnjs.indexOf(s1) : idPjs.indexOf(s1);
            Combatiente c1 = pjs.get(id1);
            int id2 = (idPjs.indexOf(s2) < 0) ? idPnjs.indexOf(s2) : idPjs.indexOf(s2);
            Combatiente c2 = pjs.get(id2);
            return c1.compareTo(c2);
          }
        });
        this.orden.get(i).clear();
        this.orden.get(i).addAll(Arrays.asList(c));
      }
    }
  }


}
