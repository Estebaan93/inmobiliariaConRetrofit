//ui/main/logout/LogoutViewModel
package com.example.inmobiliaria.ui.main.logout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;


public class LogoutViewModel extends AndroidViewModel {
  private MutableLiveData<String> mVolverAlLogin;
  private final PropietarioRepositorio repo;

  public LogoutViewModel(@NonNull Application application) {
    super(application);
    repo= new PropietarioRepositorio(application);
  }

  public LiveData<String> getVolverAlLogin() {
    if (mVolverAlLogin == null) {
      mVolverAlLogin = new MutableLiveData<>();
    }
    return mVolverAlLogin;
  }


  public void cerrarSesion(){
    repo.cerrarSesion();
    //Toast.makeText(getApplication(), "Sesion cerrada correctamente", Toast.LENGTH_SHORT).show();

    //Emitimos evento para que el fragment navegue al login
    if (mVolverAlLogin == null) {
      mVolverAlLogin = new MutableLiveData<>();
    }
    mVolverAlLogin.postValue("Sesion cerrada correctamente");
  }


}