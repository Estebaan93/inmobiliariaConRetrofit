//ui/main/inmueble/DetalleInmuebleViewModel
package com.example.inmobiliaria.ui.main.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.InmuebleRepositorio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
  private final MutableLiveData<Inmueble> inmueble = new MutableLiveData<>();
  private final MutableLiveData<String> mensaje = new MutableLiveData<>();
  private final MutableLiveData<Integer> colorTexto = new MutableLiveData<>();
  private final MutableLiveData<Integer> colorFondo = new MutableLiveData<>();
  private final MutableLiveData<Integer> visibilidad = new MutableLiveData<>();
  private final InmuebleRepositorio repo;
  private boolean cargandoDatos = true;

  public DetalleInmuebleViewModel(@NonNull Application application) {
    super(application);
    repo = new InmuebleRepositorio(application);
    ocultarMensaje();
  }

  public LiveData<Inmueble> getInmueble() { return inmueble; }
  public LiveData<String> getMensaje() { return mensaje; }
  public LiveData<Integer> getColorTexto() { return colorTexto; }
  public LiveData<Integer> getColorFondo() { return colorFondo; }
  public LiveData<Integer> getVisibilidad() { return visibilidad; }

  public void setInmuebleDesdeBundle(Bundle bundle) {
    if (bundle != null && bundle.containsKey("inmueble")) {
      Inmueble i = (Inmueble) bundle.getSerializable("inmueble");
      inmueble.setValue(i);
    }
  }

  public void marcarCargaCompleta() {
    cargandoDatos = false;
  }

  public void actualizarDisponibilidad(boolean disponible) {
    if (cargandoDatos) return;

    Inmueble i = inmueble.getValue();
    if (i == null) return;

    i.setDisponible(disponible);

    repo.actualizarInmueble(i, new Callback<Inmueble>() {
      @Override
      public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
        if (response.isSuccessful() && response.body() != null) {
          inmueble.postValue(response.body());
          exito("Disponibilidad actualizada");
          Log.d("API", "Inmueble actualizado: " + response.body().getIdInmueble());
        } else {
          error("Error al actualizar (" + response.code() + ")");
          Log.e("API", "Error PUT: " + response.code());
        }
      }

      @Override
      public void onFailure(Call<Inmueble> call, Throwable t) {
        error("Error de conexión");
        Log.e("API", "Fallo: " + t.getMessage());
      }
    });
  }

  private void error(String msg) {
    mensaje.setValue(msg);
    colorTexto.setValue(0xFFD32F2F);
    colorFondo.setValue(0xFFFFEBEE);
    visibilidad.setValue(View.VISIBLE);
  }

  private void exito(String msg) {
    mensaje.setValue(msg);
    colorTexto.setValue(0xFF2E7D32);
    colorFondo.setValue(0xFFE8F5E9);
    visibilidad.setValue(View.VISIBLE);
  }

  private void ocultarMensaje() {
    visibilidad.setValue(View.GONE);
  }
}