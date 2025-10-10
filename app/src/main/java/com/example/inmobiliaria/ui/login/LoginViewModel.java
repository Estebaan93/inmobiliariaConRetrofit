//ui/login/LoginViewModel
package com.example.inmobiliaria.ui.login;

import android.app.Application;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
  private MutableLiveData<String> mMensaje;
  public MutableLiveData<Boolean> mCargar;
  private MutableLiveData<Bundle> mIrAlmenu;

  private final PropietarioRepositorio repo;

  public LoginViewModel(@NonNull Application application) {
    super(application);
    repo= new PropietarioRepositorio(application);
    mIrAlmenu = new MutableLiveData<>();
    verificarToken();
  }

  public LiveData<String> getmMensaje(){
    if(mMensaje==null){
      mMensaje= new MutableLiveData<>();
    }
    return mMensaje;
  }

  public LiveData<Boolean> getmCargar(){
    if(mCargar==null){
      mCargar= new MutableLiveData<>();
    }
    return mCargar;
  }

  public LiveData<Bundle> getmIrAlmenu(){
    return mIrAlmenu;
  }

  // Verifica si hay token guardado para navegaciona
  private void verificarToken() {
    String token = repo.leerToken();
    if (token == null || token.isEmpty()) return;

    // Mostramos loader
    if (mCargar == null) mCargar = new MutableLiveData<>();
    mCargar.postValue(true);

    // Llamada a la API para validar token
    repo.validarToken(token, new Callback<com.example.inmobiliaria.data.model.Propietario>() {
      @Override
      public void onResponse(Call<com.example.inmobiliaria.data.model.Propietario> call, Response<com.example.inmobiliaria.data.model.Propietario> response) {
        mCargar.postValue(false);
        if (response.isSuccessful()) {
          // Token valido,  navegar al menu
          Bundle datos = new Bundle();
          datos.putString("token", token);
          mIrAlmenu.postValue(datos);
        } else {
          // Token vencido, limpiar y quedarse en login
          repo.guardarToken(null);
          mMensaje.postValue("Sesion expirada, inicie sesion nuevamente");
        }
      }

      @Override
      public void onFailure(Call<com.example.inmobiliaria.data.model.Propietario> call, Throwable t) {
        mCargar.postValue(false);
        mMensaje.postValue("No se pudo verificar la sesion");
      }
    });
  }

  public void login(String usuario, String clave){
    mCargar.postValue(true); //Muestra el loader

    if (usuario.trim().isEmpty() || clave.trim().isEmpty()) {
      mMensaje.postValue("Complete todos los campos");
      mCargar.postValue(false);
      return;
    }

    Log.d("API_LOGIN", "Intentando login con usuario: " + usuario + " y clave: " + clave);

    //Logica delegada al repo
    repo.login(usuario, clave, new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        mCargar.postValue(false); //Oculta el loader

        if(response.isSuccessful()){
          String token= response.body();
          repo.guardarToken(token);

          Log.d("API_LOGIN_TOKEN", "token: " + token);

          Bundle bundle= new Bundle();
          bundle.putString("usuario", usuario);
          bundle.putString("token", token);

          mMensaje.postValue("Bienvenido");
          mIrAlmenu.postValue(bundle);
        }else{
          mMensaje.postValue("Usuario o contraseña incorrectos");
        }

      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        mCargar.postValue(false);
        Log.e("API_LOGIN_ERROR", "Error de conexión: " + t.getMessage(), t);
        mMensaje.postValue("Error de conexion con el servidor");
      }
    });

  }

}
