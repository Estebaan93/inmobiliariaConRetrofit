//data/request/InmobiliariaService
package com.example.inmobiliaria.data.request;

import com.example.inmobiliaria.data.model.Propietario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface InmobiliariaService {

  //login de azuere/ login api local
  @FormUrlEncoded
  @POST("api/Propietarios/login")
  Call<String> login(@Field("Usuario") String usuario, @Field("Clave")String clave);


  //Obtener perfil de azure
  /*@GET("api/Propietarios")
  Call<Propietario> obtenerPerfil(@Header("Authorization") String token);*/

  //Actualizar perfil de azure
  /*@PUT("api/Propietarios/actualizar")
  Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);*/

  //Cambiar contraseña de azure
  /*@FormUrlEncoded
  @PUT("api/Propietarios/changePassword")
  Call<Void> cambiarPassword(@Header("Authorization") String token, @Field("currentPassword") String currentPassword, @Field("newPassword")String newPassword);*/




  //---LOCAL-----
  //Obtener perfil de api local
  @GET("api/Propietarios/perfil")
  Call<Propietario> obtenerPerfil(@Header("Authorization") String token);

  //Actualizar perfil de api local
  @PUT("api/Propietarios/editar")
  Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

  //Cambiar password api local
  @FormUrlEncoded
  @PUT("api/Propietarios/cambiarpassword")
  Call<Void> cambiarPassword(@Header("Authorization") String token, @Field("currentPassword") String currentPassword, @Field("newPassword")String newPassword);



}
