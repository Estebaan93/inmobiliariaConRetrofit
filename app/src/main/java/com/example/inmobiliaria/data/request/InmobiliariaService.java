//data/request/InmobiliariaService
package com.example.inmobiliaria.data.request;

import com.example.inmobiliaria.data.model.Propietario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InmobiliariaService {
  @FormUrlEncoded
  @POST("api/Propietarios/login")
  Call<String> login(@Field("Usuario") String usuario, @Field("Clave")String clave);

  @GET("api/Propietarios")
  Call<Propietario> obtenerPerfil(@Header("Authorization") String token);
}
