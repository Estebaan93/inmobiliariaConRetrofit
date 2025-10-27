//data/model/Propietario
package com.example.inmobiliaria.data.model;

import java.io.Serializable;

public class Propietario implements Serializable {//

  //Los campos deben ser lo mismos que la BD (azure en este caso)
  private int idPropietario;
  private int dni;
  private String apellido;
  private String nombre;
  private String telefono;
  private String email;
  private String clave;


  public Propietario(int idPropietario, int dni, String apellido, String nombre, String telefono, String email, String clave) {
    /*Aca los nombres puede ser cualquiera*/
    this.idPropietario = idPropietario;
    this.dni = dni;
    this.apellido = apellido;
    this.nombre = nombre;
    this.telefono = telefono;
    this.email = email;
    this.clave = clave;
  }

  public Propietario(){
  }

  public int getIdPropietario() {
    return idPropietario;
  }

  public void setIdPropietario(int idPropietario) {
    this.idPropietario = idPropietario;
  }

  public int getDni() {
    return dni;
  }

  public void setDni(int dni) {
    this.dni = dni;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }
}
