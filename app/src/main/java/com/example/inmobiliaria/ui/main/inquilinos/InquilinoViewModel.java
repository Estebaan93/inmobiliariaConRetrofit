//ui/main/inquilinos/InquilinoViewModel
package com.example.inmobiliaria.ui.main.inquilinos;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.data.repositorio.InquilinoRepositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {
  private final MutableLiveData<List<Inmueble>> mLista = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mCargando = new MutableLiveData<>();
  private final InquilinoRepositorio repo;
  private List<Inmueble> inmueblesConInquilinos = new ArrayList<>();

  public InquilinoViewModel(@NonNull Application application) {
    super(application);
    repo=new InquilinoRepositorio(application);
    cargarLista();
  }
  public LiveData<List<Inmueble>> getMLista() {

    return mLista;
  }
  public LiveData<String> getMError() {

    return mError;
  }
  public LiveData<Boolean> getMCargando() {
    return mCargando;
  }

  private void cargarLista() {
    mCargando.setValue(true);
    repo.obtenerTodosLosInmuebles(new Callback<List<Inmueble>>() {
      @Override
      public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
        if (response.isSuccessful() && response.body() != null) {
          List<Inmueble> todosInmuebles = response.body();

          if (todosInmuebles.isEmpty()) {
            mCargando.postValue(false);
            mError.setValue("No hay inmuebles registrados");
            mLista.postValue(new ArrayList<>());
          } else {
            // Filtrar inmuebles que tienen contratos (tuvieron)
            filtrarInmueblesConContratos(todosInmuebles);
          }
        } else {
          mCargando.postValue(false);
          mError.setValue("Error al obtener inmuebles");
        }
      }

      @Override
      public void onFailure(Call<List<Inmueble>> call, Throwable t) {
        mCargando.postValue(false);
        mError.setValue("Error del servidor");
      }
    });
  }

  /*Filtra los inmuebles que tienen/tuvieron contratos*/
  private void filtrarInmueblesConContratos(List<Inmueble> todosInmuebles) {
    inmueblesConInquilinos.clear();
    verificarContratosPorInmueble(todosInmuebles, 0);
  }

  /*Verifica recursivamente si cada inmueble tiene contrato*/
  private void verificarContratosPorInmueble(List<Inmueble> inmuebles, int index) {
    if (index >= inmuebles.size()) {
      mCargando.postValue(false);
      // Terminamos de verificar todos
      if (inmueblesConInquilinos.isEmpty()) {
        mError.setValue("No hay inmuebles con inquilinos");
        mLista.postValue(new ArrayList<>());
      } else {
        mLista.postValue(inmueblesConInquilinos);
      }
      return;
    }

    Inmueble inmueble = inmuebles.get(index);

    repo.obtenerContratoPorInmueble(inmueble.getIdInmueble(), new Callback<Contrato>() {
      @Override
      public void onResponse(Call<Contrato> call, Response<Contrato> response) {
        if (response.isSuccessful() && response.body() != null) {
          // Este inmueble tiene contrato
          inmueblesConInquilinos.add(inmueble);
        }
        // Continuar con el siguiente
        verificarContratosPorInmueble(inmuebles, index + 1);
      }

      @Override
      public void onFailure(Call<Contrato> call, Throwable t) {
        // Si falla, continuar sin agregarlo
        verificarContratosPorInmueble(inmuebles, index + 1);
      }
    });
  }





}