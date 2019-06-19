package com.example.ulric.avispro.modelos;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ulric.avispro.interfaces.MyCallbackData;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Personaje implements Serializable, Comparable<Personaje> {

  @Expose @SerializedName("avatar") private String avatar;
  @Expose @SerializedName("caracteristicas") private Map<String, ArrayList<Long>> caracteristicas;
  @Expose @SerializedName("cultura") private String cultura;
  @Expose @SerializedName("edad") private int edad;
  @Expose @SerializedName("idPersonaje") private String idPersonaje;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("nivel") private int nivel;
  @Expose @SerializedName("procedencia") private String procedencia;
  @Expose @SerializedName("raza") private String raza;
  @Expose @SerializedName("idHabilidades") private List<String> idHabilidades;
  @Expose @SerializedName("habilidades") private final List<Habilidad> habilidades =
      Collections.synchronizedList(new ArrayList<Habilidad>());

  public Personaje() {
    this.avatar = "";
    final ArrayList<Long> min = new ArrayList<Long>() {{ add(0,Long.valueOf("4")); add(1,Long.valueOf("1")); }};
    this.caracteristicas = new HashMap<String, ArrayList<Long>>() {{
      put("Agi", min); put("Apa", min); put("Con", min); put("Des", min);
      put("Emp", min); put("For", min); put("Int", min); put("Mem", min);
      put("Ref", min); put("Per", min); put("Pod", min); put("Vol", min);
    }};
    this.idHabilidades = new ArrayList<>();
  }
  public Personaje(String avatar, Map<String, ArrayList<Long>> caracteristicas, String cultura,
                   int edad, String idPersonaje, String nombre, int nivel, String procedencia,
                   String raza, List<String> idHabilidades) {
    this.avatar =          avatar;
    this.caracteristicas = caracteristicas;
    this.cultura =         cultura;
    this.edad =            edad;
    this.idPersonaje =     idPersonaje;
    this.nombre =          nombre;
    this.nivel =           nivel;
    this.procedencia =     procedencia;
    this.raza =            raza;
    this.idHabilidades =   idHabilidades;
  }

  /**      Getters      **/
  public String getAvatar() { return avatar; }
  public Map<String, ArrayList<Long>> getCaracteristicas() { return caracteristicas; }
  public String getCultura() { return cultura; }
  public int getEdad() { return edad; }
  public String getIdPersonaje() { return idPersonaje; }
  public String getNombre() { return nombre; }
  public int getNivel() { return nivel; }
  public String getProcedencia() { return procedencia; }
  public String getRaza() { return raza; }
  public List<String> getIdHabilidades() { return idHabilidades; }
  public List<Habilidad> getHabilidadesPersonaje() { return habilidades; }


  /**      Setters      **/
  public void setAvatar(String avatar) { this.avatar = avatar; }
  public void setCaracteristicas(Map<String, ArrayList<Long>> caracteristicas) { this.caracteristicas = caracteristicas; }
  public void setCultura(String cultura) { this.cultura = cultura; }
  public void setEdad(int edad) { this.edad = edad; }
  public void setIdPersonaje(String idPersonaje) { this.idPersonaje = idPersonaje; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setNivel(int nivel) { this.nivel = nivel; }
  public void setProcedencia(String procedencia) { this.procedencia = procedencia; }
  public void setRaza(String raza) { this.raza = raza; }
  public void setIdHabilidades(List<String> idHabilidades) { this.idHabilidades = idHabilidades; }


  @Override
  public int compareTo(Personaje p) {
    return this.nombre.compareTo(p.getNombre());
  }


  /**      Métodos Públicos      **/

  /**
   * Añadir una habilidad al personaje mediante el identificador de la habilidad
   * @param id, identificador de la habilidad a añadir al personaje
   */
  public void anadirIdHabilidad(String id){
    this.idHabilidades.add(id);
    FirebaseFirestore.getInstance().collection("personajes").document(this.idPersonaje)
        .update("idHabilidades", this.getIdHabilidades());
  }

  /**
   * Eliminar habilidad del personaje y actualizar Firebase
   * @param id, identificador de la habilidad a eliminar
   */
  public void borrarHabilidadPersonaje(String id) {
    this.idHabilidades.remove(id);
    this.guardarPersonajeHabilidad();
  }

  /**
   * Eliminar personaje de Firebase
   */
  public void borrarPersonaje(){
    FirebaseFirestore.getInstance().collection("personajes")
      .document(this.idPersonaje).delete();
  }

  /**
   * Descargar desde Firebase Storage a un archivo local el avatar del personaje
   * @param contexto, contexto de la aplicación
   */
  public void cargarAvatar(final Context contexto) {
    if (!avatar.equals("")) {
      final String name = avatar.substring(0, (avatar.lastIndexOf(".")));
      final String ext = avatar.substring((avatar.lastIndexOf(".") + 1));
      File dir = new File(contexto.getFilesDir() + "/avatars/");
      try {
        final File oldfile = new File(dir, avatar);
        if (oldfile.exists()) {
          oldfile.delete();
        }
        final File localFile = File.createTempFile(name, ".temp", dir);
        String av = String.format("%s/%s.%s", "avatars", name, ext);
        StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child(av);
        Log.d("AvatarRef: ", "Path: " + avatarRef.getPath());
        Log.d("AvatarRef: ", "Name: " + avatarRef.getName());
        Log.d("AvatarRef: ", "Bucket: " + avatarRef.getBucket());
        avatarRef.getFile(localFile)
            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
              }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  e.printStackTrace();
                  Log.e("Personaje: ", "Load file from Firebase error.");
                }
            }).addOnCanceledListener(new OnCanceledListener() {
              @Override
              public void onCanceled() {
                Log.e("Personaje: ", "Canceled.");
              }
            });
        avatar = String.format("%s.%s", name, ext);
        Boolean success = localFile.renameTo(new File(contexto.getFilesDir() + "/avatars/", avatar));
        Log.d("Personaje: ", "Creación del archivo: " + success);
      } catch (java.io.IOException e) {
        e.printStackTrace();
        Log.e("Personaje: ", "IOException");
      }
    }
  }

  /**
   * Conseguir la Uri del archivo del avatar del personaje.
   * @param contexto, contexto desde el que se llama a la función.
   * @return Uri, del archivo del avatar del personaje.
   */
  public Uri conseguirAvatarUri(final Context contexto) {
    File file = new File(contexto.getFilesDir()+ "/avatars/", avatar);
    Boolean recarga = false;
    while (!file.exists()) {
      if (!recarga) {
        cargarAvatar(contexto);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            File file = new File(contexto.getFilesDir() + "/avatars/", avatar);
          }
        }, 5000);
        recarga = true;
      }
    }
    return Uri.fromFile(file);
  }

  /**
   * Obtener la iniciativa, reflejos, del personaje.
   * @return Long, valor de la iniciativa del personaje.
   */
  public Long conseguirIniciativa() { return caracteristicas.get("Ref").get(1); }

  /**
   * Guardar un nuevo avatar del personaje en Firebase Storage.
   * @param pfd, descriptor del archivo imagen del avatar.
   * @param name, nombre del archivo imagen del avatar.
   */
  public void guardarNuevoAvatar(ParcelFileDescriptor pfd, String name) {
    InputStream stream = new FileInputStream(pfd.getFileDescriptor());
    final String avatar = idPersonaje + name.substring(name.lastIndexOf("."));
    FirebaseStorage.getInstance().getReference().child("avatars/" + avatar).putStream(stream)
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
          Log.d("Personaje, Set Avatar: ", exception.getLocalizedMessage());
        }
      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
          FirebaseFirestore.getInstance().collection("personajes").document(idPersonaje)
            .update("avatar", avatar);
          setAvatar(avatar);
        }
      });
  }

  /**
   * Guardar un personaje en Firebase
   * @param usuario, usuario al que asignar el personaje nuevo
   * @param mcp, callback donde devolvemos el personaje
   */
  public void guardarPersonaje(final Usuario usuario, final MyCallbackData mcp) {
    final Personaje p = new Personaje(this.getAvatar(), this.getCaracteristicas(), this.getCultura(),
        this.getEdad(), this.getIdPersonaje(), this.getNombre(), this.getNivel(),
        this.getProcedencia(), this.getRaza(), this.getIdHabilidades());
    if (p.getIdPersonaje() != null) {
      FirebaseFirestore.getInstance().collection("personajes").document(p.getIdPersonaje())
        .set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("Personaje: ", "Personaje Guardado Exitosamente");
          mcp.onCallbackData(p);
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("Personaje: ", "Personaje No Guardado Exitosamente");
          mcp.onCallbackData(p);
        }
      });
    } else {
      FirebaseFirestore.getInstance().collection("personajes").add(p)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            String id = documentReference.getId();
            FirebaseFirestore.getInstance().collection("personajes")
              .document(id).update("idPersonaje", id);
            usuario.guardarPersonaje(id);
            p.setIdPersonaje(id);
            Log.d("Personaje: ", "Personaje Nuevo Guardado Exitosamente");
            mcp.onCallbackData(p);
          }
        });
    }
  }

  /**
   * Guardar los identificadores de las habilidades del personaje en Firebase
   */
  public void guardarPersonajeHabilidad() {
    FirebaseFirestore.getInstance().collection("personajes")
      .document(this.idPersonaje)
      .update("idHabilidades", this.getIdHabilidades())
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("Personaje: ", "Personaje Guardado Exitosamente");
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("Personaje: ", "Personaje No Guardado Exitosamente");
        }
      });
  }

  /**
   * Obtener desde Firebase los datos completos, base y particulares, de una habilidad del personaje.
   * @param mcp, función de retorno.
   */
  public void obtenerHabilidadesPersonaje(final MyCallbackData mcp) {
    for(String idHabilidad: this.idHabilidades) {
      Habilidad.obtenerHabilidadPersonaje(idHabilidad, new MyCallbackData() {
        @Override
        public void onCallbackData(Object objeto) {
          final Habilidad habilidadPersonaje = (Habilidad) objeto;
          habilidadPersonaje.obtenerHabilidadGeneral(new MyCallbackData() {
            @Override
            public void onCallbackData(Object objeto) {
              Habilidad habilidad = (Habilidad) objeto;
              habilidad.completarHabilidad(habilidadPersonaje);
              habilidades.add(habilidad);
            }
          });
        }
      });
    }
    mcp.onCallbackData(habilidades);
  }

  /**
   * Recargar los datos del personaje desde Firebase
   * @param pj, personaje a recargar
   */
  public void recargarPersonaje(final Personaje pj) {
    FirebaseFirestore.getInstance().collection("personajes").document(this.idPersonaje).get()
      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
              Log.d("Usuario", "DocumentSnapshot data: " + document.getData());
              Personaje p = document.toObject(Personaje.class);
              pj.setAvatar(p.getAvatar());
              pj.setCaracteristicas(p.getCaracteristicas());
              pj.setCultura(p.getCultura());
              pj.setEdad(p.getEdad());
              pj.setIdHabilidades(p.getIdHabilidades());
              pj.setIdPersonaje(p.getIdPersonaje());
              pj.setNivel(p.getNivel());
              pj.setNombre(p.getNombre());
              pj.setProcedencia(p.getProcedencia());
              pj.setRaza(p.getRaza());
            } else {
              Log.d("Usuario", "No such document");
            }
          } else {
            Log.d("Usuario", "get failed with ", task.getException());
          }
        }
      });
  }

  /**
   * Devuelve el bonus principal (-10 a 10) de una característica
   * @param caracteristica, la caracterisitca de la que queremos saber su bonus principal
   * @return bonus principal (-10 a 10) de la característica dada
   */
  public int valorBonusPrincipal(String caracteristica) {
    ArrayList a = this.caracteristicas.get(capitalize(caracteristica));
    int valor = Integer.parseInt(a.get(1).toString());
    switch(valor) {
      case 40: return 10;
      case 39: return  8;
      case 38: return  7;
      case 37: return  6;
      case 36: return  5;
      case 35: case 34: return 4;
      case 33: case 32: case 31: return 3;
      case 30: case 29: case 28: case 27: case 26: return 2;
      case 25: case 24: case 23: case 22: case 21: return 1;
      case 15: case 14: case 13: case 12: return -1;
      case 11: case 10: case 9: return -2;
      case  8: case  7: return -3;
      case  6: return  -4;
      case  5: return  -5;
      case  4: return  -6;
      case  3: return  -7;
      case  2: return  -8;
      case  1: return -10;
      default: return 0;
    }
  }

  /**
   * Devuelve el bonus secundario (-4 a 4) de una característica
   * @param caracteristica, la caracterisitca de la que queremos saber su bonus secundario
   * @return bonus secundario (-4 a 4) de la característica dada
   */
  public int valorBonusSecundario(String caracteristica) {
    ArrayList a = this.caracteristicas.get(capitalize(caracteristica));
    int valor = Integer.parseInt(a.get(1).toString());
    switch(valor) {
      case 40: return 4;
      case 39: case 38: return 3;
      case 37: case 36: return 2;
      case 35: case 34: case 33: case 32: case 31: return 1;
      case 30: case 29: case 28: case 27: case 26: return 2;
      case  8: case  7: case  6: return -1;
      case  5: case  4: return -2;
      case  3: case  2: return -3;
      case  1: return -4;
      default: return 0;
    }
  }

  /**      Métodos Privados      **/

  /**
   * Devolvemos la cadena con el primer caracter en mayúscula y el resto en minúsculas
   * @param cadena, cadena a capitalizar
   * @return String, cadena con el primer caracter en mayúscula y el resto en minúsculas
   */
  @NonNull
  private String capitalize(@NonNull String cadena){
    return cadena.substring(0,1).toUpperCase().concat(cadena.substring(1).toLowerCase());
  }

}
