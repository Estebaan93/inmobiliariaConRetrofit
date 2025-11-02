//ui/main/contratos/ContratoViewModel
package com.example.inmobiliaria.ui.main.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.ContratoRepositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoViewModel extends AndroidViewModel {
  private final MutableLiveData<List<Inmueble>> mLista = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mCargando = new MutableLiveData<>();
  private final ContratoRepositorio repo;
  public ContratoViewModel(@NonNull Application application) {
    super(application);
    repo = new ContratoRepositorio(application);
    cargarContratosVigentes();
  }
  public LiveData<List<Inmueble>> getMLista() { return mLista; }
  public LiveData<String> getMError() { return mError; }
  public LiveData<Boolean> getMCargando() { return mCargando; }


  public void cargarContratosVigentes() {
    mCargando.setValue(true);

    // Llamamos al metodo del repositorio
    repo.obtenerInmueblesAlquilados(new Callback<List<Inmueble>>() {
      @Override
      public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
        mCargando.postValue(false);
        // Validamos que la API responda exitosamente Y que la lista no este vacia
        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
          // Esta es la lista de Inmuebles con contrato vigente
          mLista.postValue(response.body());
        } else {
          mError.setValue("No se encontraron contratos vigentes");
          mLista.postValue(new ArrayList<>());
        }
      }

      @Override
      public void onFailure(Call<List<Inmueble>> call, Throwable t) {
        mCargando.postValue(false);
        mError.setValue("Error del servidor: " + t.getMessage());
      }
    });
  }

  // TODO: Implement the ViewModel
}