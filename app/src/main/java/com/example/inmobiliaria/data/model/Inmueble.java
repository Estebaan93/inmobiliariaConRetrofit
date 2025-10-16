//data/model/Inmueble
package com.example.inmobiliaria.data.model;

import java.io.Serializable;

public class Inmueble implements Serializable {
  private int idInmueble;
  private String direccion;


  private double valor;
  private Propietario propietario;
  private String uso;
  private String tipo;
  private int ambientes;

  private int superficie;
  private double latitud;
  private double longitud;

  private String imagen;
  private boolean disponible;

  public Inmueble(int idInmueble, String direccion, double valor, Propietario propietario, String uso, String tipo, int ambientes, int superficie, double latitud, double longitud, String imagen, boolean disponible) {
    this.idInmueble = idInmueble;
    this.direccion = direccion;
    this.valor = valor;
    this.propietario = propietario;
    this.uso = uso;
    this.tipo = tipo;
    this.ambientes = ambientes;
    this.superficie = superficie;
    this.latitud = latitud;
    this.longitud = longitud;
    this.imagen = imagen;
    this.disponible = disponible;
  }

  public Inmueble() {
  }

  public int getIdInmueble() {
    return idInmueble;
  }

  public void setIdInmueble(int idInmueble) {
    this.idInmueble = idInmueble;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public Propietario getPropietario() {
    return propietario;
  }

  public void setPropietario(Propietario propietario) {
    this.propietario = propietario;
  }

  public String getUso() {
    return uso;
  }

  public void setUso(String uso) {
    this.uso = uso;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public int getAmbientes() {
    return ambientes;
  }

  public void setAmbientes(int ambientes) {
    this.ambientes = ambientes;
  }

  public int getSuperficie() {
    return superficie;
  }

  public void setSuperficie(int superficie) {
    this.superficie = superficie;
  }

  public double getLatitud() {
    return latitud;
  }

  public void setLatitud(double latitud) {
    this.latitud = latitud;
  }

  public double getLongitud() {
    return longitud;
  }

  public void setLongitud(double longitud) {
    this.longitud = longitud;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }

  public boolean isDisponible() {
    return disponible;
  }

  public void setDisponible(boolean disponible) {
    this.disponible = disponible;
  }
}
