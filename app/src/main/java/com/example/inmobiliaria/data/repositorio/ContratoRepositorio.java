package com.example.inmobiliaria.data.repositorio;

import android.app.Application;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.model.Pago;
import com.example.inmobiliaria.data.preferencias.SessionManager;
import com.example.inmobiliaria.data.request.ApiClient;
import com.example.inmobiliaria.data.request.InmobiliariaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ContratoRepositorio {
  private final InmobiliariaService api;
  private final SessionManager sessionManager;

  public ContratoRepositorio(Application app) {
    api = ApiClient.getApiInmobiliaria();
    sessionManager = new SessionManager(app);
  }

  //Para la lista de contratosFragmet
  public void obtenerInmueblesAlquilados(Callback<List<Inmueble>> callback){
    String token = sessionManager.leerToken();
    if(token == null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<List<Inmueble>> llamada = api.obtenerInmueblesAlquilados("Bearer " + token);
    llamada.enqueue(callback);
  }

  // Para DetalleContratoFragment
  public void obtenerContratoPorInmueble(int idInmueble, Callback<Contrato> callback){
    String token = sessionManager.leerToken();
    if(token == null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<Contrato> llamada = api.obtenerContratoPorInmueble("Bearer " + token, idInmueble);
    llamada.enqueue(callback);
  }

  // Para PagosFragment
  public void obtenerPagosPorContrato(int idContrato, Callback<List<Pago>> callback){
    String token = sessionManager.leerToken();
    if(token == null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }
    Call<List<Pago>> llamada = api.obtenerPagosPorContrato("Bearer " + token, idContrato);
    llamada.enqueue(callback);
  }


}
