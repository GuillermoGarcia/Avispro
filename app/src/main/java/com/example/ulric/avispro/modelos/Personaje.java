package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Personaje implements Serializable {

  @Expose @SerializedName("avatar") private String avatar;
  @Expose @SerializedName("cultura") private String cultura;
  @Expose @SerializedName("edad") private int edad;
  @Expose @SerializedName("idJugador") private String idJugador;
  @Expose @SerializedName("idPersonaje") private int idPersonaje;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("nivel") private int nivel;
  @Expose @SerializedName("procedencia") private String procedencia;
  @Expose @SerializedName("raza") private String raza;

  public Personaje() { }

  public Personaje(String avatar, String cultura, int edad, String idJugador, int idPersonaje, String nombre,
                   int nivel, String procedencia, String raza) {
    this.avatar = avatar;
    this.cultura = cultura;
    this.edad = edad;
    this.idJugador = idJugador;
    this.idPersonaje = idPersonaje;
    this.nombre = nombre;
    this.nivel = nivel;
    this.procedencia = procedencia;
    this.raza = raza;
  }

  public String getAvatar() { return avatar; }
  public String getCultura() { return cultura; }
  public int getEdad() { return edad; }
  public String getIdJugador() { return idJugador; }
  public int getIdPersonaje() { return idPersonaje; }
  public String getNombre() { return nombre; }
  public int getNivel() { return nivel; }
  public String getProcedencia() { return procedencia; }
  public String getRaza() { return raza; }

  public void setAvatar(String avatar) { this.avatar = avatar; }
  public void setCultura(String cultura) { this.cultura = cultura; }
  public void setEdad(int edad) { this.edad = edad; }
  public void setIdJugador(String idJugador) { this.idJugador = idJugador; }
  public void setIdPersonaje(int idPersonaje) { this.idPersonaje = idPersonaje; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setNivel(int nivel) { this.nivel = nivel; }
  public void setProcedencia(String procedencia) { this.procedencia = procedencia; }
  public void setRaza(String raza) { this.raza = raza; }

}
