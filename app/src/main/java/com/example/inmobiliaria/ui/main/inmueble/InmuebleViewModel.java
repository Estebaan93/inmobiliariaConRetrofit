//ui/main/inmueble/InmuebleViewModel
package com.example.inmobiliaria.ui.main.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
  private MutableLiveData<List<Inmueble>> listaInmuebles= new MutableLiveData<>();
  private final MutableLiveData<String> mensaje = new MutableLiveData<>();
  private final PropietarioRepositorio repo;
  public InmuebleViewModel(@NonNull Application application) {
    super(application);
    repo= new PropietarioRepositorio(application);
    cargarInmueblesAPI();
  }

  public LiveData<String>getMensaje(){
    return mensaje;
  }
  public LiveData<List<Inmueble>> getListaInmuebles(){
    return listaInmuebles;
  }

  public void cargarInmueblesAPI(){
    repo.obtenerInmuebles(new Callback<List<Inmueble>>(){
      @Override
      public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
        if (response.isSuccessful() && response.body() != null) {
          listaInmuebles.postValue(response.body());
        } else {
          mensaje.postValue("Error al obtener inmuebles (" + response.code() + ")");
        }
      }

      @Override
      public void onFailure(Call<List<Inmueble>> call, Throwable t) {
        mensaje.postValue("Fallo de conexion: " + t.getMessage());
      }
    });
  }

}