package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Equipo {

  @Expose @SerializedName("idEquipo") private int idEquipo;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("peso") private int peso;
  @Expose @SerializedName("descripcion") private String descripcion;
  @Expose @SerializedName("esArma") private boolean esArma;


  public Equipo() {}

  public Equipo(int idEquipo, String nombre, int peso, String descripcion, boolean esArma) {
    this.idEquipo = idEquipo;
    this.nombre = nombre;
    this.peso = peso;
    this.descripcion = descripcion;
    this.esArma = esArma;
  }

  public String getDescripcion() { return descripcion; }
  public int getIdEquipo() { return idEquipo; }
  public String getNombre() { return nombre; }
  public int getPeso() { return peso; }

  public boolean isArma() { return esArma; }

  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public void setEsArma(boolean esArma) { this.esArma = esArma; }
  public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setPeso(int peso) { this.peso = peso; }




}
