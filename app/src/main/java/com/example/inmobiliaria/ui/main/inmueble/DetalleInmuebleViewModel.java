//ui/main/inmueble/DetalleInmuebleViewModel
package com.example.inmobiliaria.ui.main.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
  //recibe el bundle(bundle) y se la muestra a la vista
  private final MutableLiveData<Inmueble> inmueble = new MutableLiveData<>();
  private final MutableLiveData<String> mensaje = new MutableLiveData<>();
  private final PropietarioRepositorio repo;
  private boolean cargandoDatos=true; //evitar que salga el toast con inmu aactuazliado

  public DetalleInmuebleViewModel(@NonNull Application application) {
    super(application);
    repo = new PropietarioRepositorio(application);
  }
  // TODO: Implement the ViewModel

  //LiveData observables
  public LiveData<Inmueble> getInmueble() {
    return inmueble;
  }

  public LiveData<String> getMensaje() {
    return mensaje;
  }

  //Seteamos el inmueble recibido desde el Bundle
  public void setInmuebleDesdeBundle(Bundle bundle) {
    if (bundle != null && bundle.containsKey("inmueble")) {
      Inmueble i = (Inmueble) bundle.getSerializable("inmueble");
      inmueble.setValue(i);
      //cargandoDatos=false;  //despues q caego habilitamos eventos
    }
  }

  public void marcarCargaCompleta(){
    cargandoDatos=false;
  }

  //Actualiza la disponibilidad (CheckBox) en el backend Azure
  public void actualizarDisponibilidad(boolean disponible) {
    if(cargandoDatos) return;

    Inmueble i = inmueble.getValue();
    if (i == null) return;

    i.setDisponible(disponible);

    repo.actualizarInmueble(i, new Callback<Inmueble>() {
      @Override
      public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
        if (response.isSuccessful() && response.body() != null) {
          inmueble.postValue(response.body());
          mensaje.postValue("Disponibilidad actualizada correctamente");
          Log.d("API", "Inmueble actualizado: " + response.body().getIdInmueble());
        } else {
          mensaje.postValue("Error al actualizar: " + response.code());
          Log.e("API", "Error PUT /api/Inmuebles/actualizar → " + response.code());
        }
      }

      @Override
      public void onFailure(Call<Inmueble> call, Throwable t) {
        mensaje.postValue("Error de conexion: " + t.getMessage());
        Log.e("API", "Fallo conexion Azure: " + t.getMessage());
      }
    });
  }




}