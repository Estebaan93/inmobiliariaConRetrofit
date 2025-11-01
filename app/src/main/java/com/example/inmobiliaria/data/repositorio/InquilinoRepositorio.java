package com.example.inmobiliaria.data.repositorio;

import android.app.Application;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.data.preferencias.SessionManager;
import com.example.inmobiliaria.data.request.ApiClient;
import com.example.inmobiliaria.data.request.InmobiliariaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class InquilinoRepositorio {
  private final InmobiliariaService api;
  private final SessionManager sessionManager;


  public InquilinoRepositorio(Application app){
    api= ApiClient.getApiInmobiliaria();
    sessionManager= new SessionManager(app);

  }

  //Obtener inmuebles para el id
  public void obtenerTodosLosInmuebles(Callback<List<Inmueble>> callback){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<List<Inmueble>> llamada= api.obtenerInmuebles("Bearer "+token);
    llamada.enqueue(callback);
  }

  //Contrato vigentes ContratoRepositorio
  /*public void obtenerInmueblesAlquilados(Callback<List<Inmueble>> callback){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    //Llamamos a la api
    Call<List<Inmueble>> llamada= api.obtenerInmueblesAlquilados("Bearer "+token);
    llamada.enqueue(callback);
  }*/

  //Obtenr inquilino por id de inmu
  public void obtenerContratoPorInmueble(int idInmueble, Callback<Contrato> callback){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<Contrato> llamada= api.obtenerContratoPorInmueble("Bearer "+token, idInmueble);
    llamada.enqueue(callback);
  }













  /*public void obtenerInmueblesAlquilados(Callback<List<Inmueble>> callback){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
      //Llamamos a la api
    Call<List<Inmueble>> llamada= api.obtenerInmueblesAlquilados("Bearer "+token);
    llamada.enqueue(callback);
    }*/

  //obtener el contrato que tiene el inquilino
  /*public void obtenerContratoPorInmu(int idInmueble, Callback<Contrato> callback){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<Contrato> llamada= api.obtenerContratoPorInmueble("Bearer "+token, idInmueble);
    llamada.enqueue(callback);
  }*/

}
