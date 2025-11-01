//ui/main/MainViewModel
package com.example.inmobiliaria.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Propietario;
import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
  private final MutableLiveData<String> mNombre= new MutableLiveData<>();
  private final MutableLiveData<String> mCorreo= new MutableLiveData<>();
  private final MutableLiveData<String> mMensaje= new MutableLiveData<>();
  private final PropietarioRepositorio repo;
  public MainViewModel(@NonNull Application application) {
    super(application);
    repo= new PropietarioRepositorio(application);
  }

  public LiveData<String> getmNombre() {
    return mNombre;
  }
  public LiveData<String> getmCorreo() {
    return mCorreo;
  }
  public LiveData<String> getmMensaje() {
    return mMensaje;
  }


  public void cargarDatos() {
    repo.obtenerPerfil(new Callback<Propietario>() {
      @Override
      public void onResponse(Call<Propietario> call, Response<Propietario> response) {
        if (response.isSuccessful() && response.body() != null) {
          Propietario propietario = response.body();

          //
          String nombreCompleto = propietario.getNombre() + " " + propietario.getApellido();

          //
          mNombre.postValue(nombreCompleto);
          mCorreo.postValue(propietario.getEmail());
          //
        } else {
          mMensaje.postValue("No se pudieron cargar los datos del perfil (" + response.code() + ")");
        }
      }
      @Override
      public void onFailure(Call<Propietario> call, Throwable t) {
        mMensaje.postValue("Error de conexin: " + t.getMessage());
      }
    });
  }


}
