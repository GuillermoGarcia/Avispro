package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Habilidad implements Serializable {

  @Expose @SerializedName("idHabilidad") private int idHabilidad;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("bonusPrincipal") private String bonusPrincipal;
  @Expose @SerializedName("bonusSecundario") private String bonusSecundario;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("tipo") private int tipo;
  @Expose @SerializedName("combate") private boolean combate;

  public Habilidad() {}

  public Habilidad(int idHabilidad, String nombre, String bonusPrincipal, String bonusSecundario,
                   String descripcion, int tipo, boolean combate) {
    this.bonusPrincipal = bonusPrincipal;
    this.bonusSecundario = bonusSecundario;
    this.combate = combate;
    this.descripcion = descripcion;
    this.idHabilidad = idHabilidad;
    this.nombre = nombre;
    this.tipo = tipo;
  }

  public String getBonusPrincipal() { return bonusPrincipal; }
  public String getBonusSecundario() { return bonusSecundario; }
  public String getDescripcion() { return descripcion; }
  public int getIdHabilidad() { return idHabilidad; }
  public String getNombre() { return nombre; }
  public int getTipo() { return tipo; }
  public boolean isCombate() { return combate; }

  public void setBonusPrincipal(String bonusPrincipal) { this.bonusPrincipal = bonusPrincipal; }
  public void setBonusSecundario(String bonusSecundario) { this.bonusSecundario = bonusSecundario; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setIdHabilidad(int idHabilidad) { this.idHabilidad = idHabilidad; }
  public void setCombate(boolean combate) { this.combate = combate; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setTipo(int tipo) { this.tipo = tipo; }
}
