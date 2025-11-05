//ui/login/LoginViewModel
package com.example.inmobiliaria.ui.login;

import android.app.Application;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
  private MutableLiveData<String> mMensaje;
  public MutableLiveData<Boolean> mCargar;
  private MutableLiveData<Bundle> mIrAlmenu;
  private final MutableLiveData<String> mRealizarLlamada = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mSolicitarPermiso = new MutableLiveData<>();
  private final PropietarioRepositorio repo;
  private static final String PHONE_NUMBER = "2664123456";
  private static final int REQUEST_CALL_PERMISSION = 1;


  // NUEVOS LiveData para visibilidad (derivados)
  private LiveData<Integer> mCargarVisibility;
  private LiveData<Integer> mMensajeVisibility;

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
  public LiveData<String> getRealizarLlamada() {
    return mRealizarLlamada;
  }

  public LiveData<Boolean> getSolicitarPermiso() {
    return mSolicitarPermiso;
  }


  //
  public LiveData<Integer> getmCargarVisibility() {
    if (mCargarVisibility == null) {
      // Transforma el LiveData<Boolean> en LiveData<Integer>
      mCargarVisibility = Transformations.map(getmCargar(), isVisible ->
              isVisible ? View.VISIBLE : View.GONE
      );
    }
    return mCargarVisibility;
  }

  public LiveData<Integer> getmMensajeVisibility() {
    if (mMensajeVisibility == null) {
      // Transforma el LiveData<String> en LiveData<Integer>
      mMensajeVisibility = Transformations.map(getmMensaje(), msg ->
              (msg != null && !msg.isEmpty()) ? View.VISIBLE : View.GONE
      );
    }
    return mMensajeVisibility;
  }
  //

  public void setInitialMessage(String msg) {
    getmMensaje(); // Asegura que mMensaje esté inicializado
    if (msg != null && !msg.isEmpty()) {
      mMensaje.postValue(msg);
    }
  }


  // Verifica si hay token guardado para navegaciona
  private void verificarToken() {
    String token = repo.leerToken();
    if (token == null || token.isEmpty()) return;

    // Mostramos loader
    getmCargar(); // Asegura inicialización//nuevo05
    mCargar.postValue(true);//nuevo05

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
          getmMensaje();//nuevo05
          mMensaje.postValue("Sesion expirada, inicie sesion nuevamente");
        }
      }

      @Override
      public void onFailure(Call<com.example.inmobiliaria.data.model.Propietario> call, Throwable t) {
        mCargar.postValue(false);
        getmMensaje();//nuevo05
        mMensaje.postValue("No se pudo verificar la sesion");
      }
    });
  }

  public void login(String usuario, String clave){
    // LIMPIAR MENSAJE ANTERIOR
    getmMensaje(); // Asegura inicializacion
    mMensaje.postValue("");

    getmCargar(); // Asegura inicializacion
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
        Log.e("API_LOGIN_ERROR", "Error de conexion: " + t.getMessage(), t);
        mMensaje.postValue("Error de conexion con el servidor");
      }
    });
  }

  public void onTelefonoAgitado(boolean tienePermiso) {
    if (tienePermiso) {
      mRealizarLlamada.postValue(PHONE_NUMBER);
    } else {
      mSolicitarPermiso.postValue(true);
    }
  }
  public void onPermisoOtorgado() {
    mRealizarLlamada.postValue(PHONE_NUMBER);
  }
  public void onPermisoDenegado() {
    mMensaje.postValue("Permiso denegado para hacer llamadas");
  }
  public void onResultadoPermiso(int requestCode, int[] grantResults) {
    if (requestCode != REQUEST_CALL_PERMISSION) return;

    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      onPermisoOtorgado();
    } else {
      onPermisoDenegado();
    }
  }


}
