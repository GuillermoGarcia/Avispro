package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personaje {

  @Expose @SerializedName("idPersonaje") private int idPersonaje;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("cultura") private String cultura;
  @Expose @SerializedName("procedencia") private String procedencia;
  @Expose @SerializedName("edad") private int edad;
  @Expose @SerializedName("idJugador") private int idJugador;

  public Personaje() { }

  public Personaje(int idPersonaje, String nombre, String cultura, String procedencia, int edad, int idJugador) {
    this.cultura = cultura;
    this.edad = edad;
    this.idJugador = idJugador;
    this.idPersonaje = idPersonaje;
    this.nombre = nombre;
    this.procedencia = procedencia;
  }

  public String getCultura() { return cultura; }
  public int getEdad() { return edad; }
  public int getIdJugador() { return idJugador; }
  public int getIdPersonaje() { return idPersonaje; }
  public String getNombre() { return nombre; }
  public String getProcedencia() { return procedencia; }

  public void setCultura(String cultura) { this.cultura = cultura; }
  public void setEdad(int edad) { this.edad = edad; }
  public void setIdJugador(int idJugador) { this.idJugador = idJugador; }
  public void setIdPersonaje(int idPersonaje) { this.idPersonaje = idPersonaje; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setProcedencia(String procedencia) { this.procedencia = procedencia; }

}
