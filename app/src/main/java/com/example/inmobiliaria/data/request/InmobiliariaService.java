//data/request/InmobiliariaService
package com.example.inmobiliaria.data.request;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.data.model.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface InmobiliariaService {

  //login de azuere/ login api local
  @FormUrlEncoded
  @POST("api/Propietarios/login")
  Call<String> login(@Field("Usuario") String usuario, @Field("Clave")String clave);


  //Obtener perfil de azure
  @GET("api/Propietarios")
  Call<Propietario> obtenerPerfil(@Header("Authorization") String token);

  //Actualizar perfil de azure
  @PUT("api/Propietarios/actualizar")
  Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

  //Cambiar contraseña de azure
  @FormUrlEncoded
  @PUT("api/Propietarios/changePassword")
  Call<Void> cambiarPassword(@Header("Authorization") String token, @Field("currentPassword") String currentPassword, @Field("newPassword")String newPassword);




  //actualizar inmu
  @PUT("api/Inmuebles/actualizar")
  Call<Inmueble> actualizarInmueble(@Header ("Authorization") String token, @Body Inmueble inmueble); //Le pasamos un obj inmueble
  //Un idInmueble y estado/disponiblidad para el check de cambiar su disponiblidad --- se puede llamar desde el detalleInmuebleViewModel


  //Obtener inmuebles
  @GET("api/Inmuebles")
  Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);


  @Multipart
  @POST("api/Inmuebles/cargar")
  Call<Inmueble> cargarInmueble(
          @Header("Authorization") String token,
          @Part MultipartBody.Part imagen,
          @Part("inmueble") RequestBody inmueble
  );


  /*Inquilinos*/
  /*Obtenemos contrato de un inmueble, trae inquilinos*/
  @GET("api/contratos/inmueble/{id}")
  Call<Contrato> obtenerContratoPorInmueble(@Header("Authorization") String token, @Path("id") int idInmueble);


  /*Contratos*/
  /* Obtenemoss los inmueble con contrato vigente y asi obtener la lista*/
  //Para contratos
  @GET("api/Inmuebles/GetContratoVigente")
  Call<List<Inmueble>> obtenerInmueblesAlquilados(@Header("Authorization") String token);



}
