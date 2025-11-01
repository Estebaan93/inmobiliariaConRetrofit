//data/repositorio/InmuebleRepositorio
package com.example.inmobiliaria.data.repositorio;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.preferencias.SessionManager;
import com.example.inmobiliaria.data.request.InmobiliariaService;
import com.google.gson.Gson;
import com.example.inmobiliaria.data.request.ApiClient;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleRepositorio {
  private final InmobiliariaService api;
  private final SessionManager sessionManager;

  public InmuebleRepositorio(Application app) {
    api = ApiClient.getApiInmobiliaria();
    sessionManager = new SessionManager(app);
  }

  /*private String getAuthHeader(){
    String token= sessionManager.leerToken();
    if(token==null || token.isEmpty()){
      return null;
    }
    return "Bearer "+token;
  }*/

  // OBTENER INMUEBLES
  public void obtenerInmuebles(Callback<List<Inmueble>> callback) {
    String token = sessionManager.leerToken();
    if (token == null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }

    Call<List<Inmueble>> llamada = api.obtenerInmuebles("Bearer " + token);
    llamada.enqueue(callback);
  }

  // ACTUALIZAR INMUEBLE
  public void actualizarInmueble(Inmueble inmueble, Callback<Inmueble> callback) {
    String token = sessionManager.leerToken();
    if (token == null || token.isEmpty()) {
      callback.onFailure(null, new Throwable("Token no disponible"));
      return;
    }

    Call<Inmueble> llamada = api.actualizarInmueble("Bearer " + token, inmueble);
    llamada.enqueue(callback);
  }

  //CARGAR INMUEBLE CON IMAGEN
  public void cargarInmueble(Inmueble inmueble, byte[] imagen, MutableLiveData<String> mensaje) {
    String token = sessionManager.leerToken();
    if (token == null || token.isEmpty()) {
      mensaje.postValue("Token invalido o no encontrado");
      return;
    }

    RequestBody inmuebleBody = RequestBody.create(
            MediaType.parse("application/json"),
            new Gson().toJson(inmueble)
    );
    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
    MultipartBody.Part imagenPart =
            MultipartBody.Part.createFormData("imagen", "foto.jpg", fileBody);

    Call<Inmueble> call = api.cargarInmueble("Bearer " + token, imagenPart, inmuebleBody);
    call.enqueue(new Callback<Inmueble>() {
      @Override
      public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
        if (response.isSuccessful()) {
          mensaje.postValue("Inmueble cargado correctamente");
        } else {
          mensaje.postValue("Error al cargar inmueble (" + response.code() + ")");
        }
      }

      @Override
      public void onFailure(Call<Inmueble> call, Throwable t) {
        mensaje.postValue("Error: " + t.getMessage());
      }
    });
  }


}
