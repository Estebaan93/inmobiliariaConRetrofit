//data/repositorio/PropietarioRepositorio
package com.example.inmobiliaria.data.repositorio;

import android.content.Context;

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
