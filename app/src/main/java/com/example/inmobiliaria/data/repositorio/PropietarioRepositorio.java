//data/repositorio/PropietarioRepositorio
package com.example.inmobiliaria.data.repositorio;

import android.content.Context;
import android.util.Log;

import com.example.inmobiliaria.data.model.Propietario;
import com.example.inmobiliaria.data.preferencias.SessionManager;
import com.example.inmobiliaria.data.request.ApiClient;
import com.example.inmobiliaria.data.request.InmobiliariaService;

import retrofit2.Call;
import retrofit2.Callback;

public class PropietarioRepositorio {
  private final InmobiliariaService api;
  private final SessionManager sessionManager;

  public PropietarioRepositorio(Context context) {
    this.api = ApiClient.getApiInmobiliaria();
    this.sessionManager= new SessionManager(context);
  }

  //Login
  public void login(String usuario, String clave, Callback<String> callback) {
    Call<String> llamada = api.login(usuario, clave);
    llamada.enqueue(callback);
  }


  //Contrtos




  //Perfil
  public void obtenerPerfil(Callback<Propietario> callback){
    String token= leerToken();
    if(token==null){
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<Propietario> llamada= api.obtenerPerfil("Bearer " + token);

    //Log.d("RESPUESTA_API","ObtenerDesdeRepo: "+ llamada);
    llamada.enqueue(callback);
  }

  //Perfil actualizar
  public void actualizarPerfil(Propietario propietario, Callback<Propietario> callback){
    String token= leerToken();
    if(token== null){
      callback.onFailure(null,new Throwable("Token no disponible"));
      return;
    }
    Call<Propietario> llamada= api.actualizarPropietario("Bearer "+token, propietario);
    llamada.enqueue(callback);
  }

  //Cambiar contraseña
  public void cambiarPassword(String actual, String nueva, Callback<Void> callback){
    String token= leerToken();
    if(token== null){
      callback.onFailure(null,new Throwable("Token no disponible"));
      return;
    }
    Call<Void> llamada= api.cambiarPassword("Bearer "+token, actual, nueva);
    llamada.enqueue(callback);
  }

  //Inmuebles




  //Contratos/pagos




  //Token en sharedPreferences
  public void guardarToken(String token){
    sessionManager.guardarToken(token);
  }
  public String leerToken(){
    return sessionManager.leerToken();
  }

  public void validarToken(String token, Callback<Propietario> callback){
    Call<Propietario> llamada = api.obtenerPerfil("Bearer " + token);
    llamada.enqueue(callback);
  }
  public void cerrarSesion() {
    sessionManager.cerrarSesion();
  }

}
