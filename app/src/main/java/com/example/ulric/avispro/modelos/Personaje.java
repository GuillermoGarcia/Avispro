package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personaje {

  @Expose @SerializedName("idPersonaje") private int idPersonaje;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("cultura") private String cultura;
  @Expose @SerializedName("procedencia") private String procedencia;
  @Expose @SerializedName("edad") private int edad;
  @Expose @SerializedName("jugador") private int jugador;
}
