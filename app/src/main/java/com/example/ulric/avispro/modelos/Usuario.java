package com.example.ulric.avispro.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {
  @Expose @SerializedName("idUsuario") private String idUsuario;
  @Expose @SerializedName("correo") private String correo;
  @Expose @SerializedName("alias") private String alias;
  @Expose @SerializedName("avatar") private String avatar;

  public Usuario (){}

  public Usuario(String idUsuario, String correo, String alias) {
    this.idUsuario = idUsuario;
    this.correo = correo;
    this.alias = alias;
  }

  public String getAlias() { return alias; }
  public String getAvatar() { return avatar; }
  public String getCorreo() { return correo; }
  public String getIdUsuario() { return idUsuario; }
  public void setAlias(String alias) { this.alias = alias; }
  public void setAvatar(String avatar) { this.avatar = avatar; }
  public void setCorreo (String correo) { this.correo = correo; }
  public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
}
