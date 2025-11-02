// ui/main/pagos/PagosViewModel
package com.example.inmobiliaria.ui.main.pagos;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Pago;
import com.example.inmobiliaria.data.repositorio.ContratoRepositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {
  private final MutableLiveData<List<Pago>> mListaPagos = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<Integer> mCargando = new MutableLiveData<>(View.GONE);
  private final MutableLiveData<String> mCodigoContrato = new MutableLiveData<>();
  private final MutableLiveData<Integer> mVisibilidadMensajeVacio = new MutableLiveData<>(View.GONE);
  private final MutableLiveData<Integer> mVisibilidadRecyclerView = new MutableLiveData<>(View.VISIBLE);
  private final ContratoRepositorio repo;

  public PagosViewModel(@NonNull Application application) {
    super(application);
    repo = new ContratoRepositorio(application);
  }

  // Getters para los LiveData
  public LiveData<List<Pago>> getMListaPagos() {
    return mListaPagos;
  }
  public LiveData<String> getMError() {
    return mError;
  }
  public LiveData<Integer> getMCargando() {
    return mCargando;
  }
  public LiveData<String> getMCodigoContrato() {
    return mCodigoContrato;
  }
  public LiveData<Integer> getMVisibilidadMensajeVacio() {
    return mVisibilidadMensajeVacio;
  }
  public LiveData<Integer> getMVisibilidadRecyclerView() {
    return mVisibilidadRecyclerView;
  }

  public void cargarPagos(Bundle bundle) {
    if (bundle == null) {
      publicarError("Error: No se recibieron datos del contrato");
      return;
    }

    int contratoId = bundle.getInt("contratoId", -1);

    if (contratoId == -1) {
      publicarError("Error: No se recibio el ID del contrato");
      return;
    }

    obtenerPagosDesdeRepositorio(contratoId);
  }

  private void obtenerPagosDesdeRepositorio(int contratoId) {
    mostrarEstadoCargando();
    mCodigoContrato.setValue(String.format("Contrato #%d", contratoId)); // String.format

    repo.obtenerPagosPorContrato(contratoId, new Callback<List<Pago>>() {
      @Override
      public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
        ocultarEstadoCargando();
        if (esRespuestaExitosa(response)) {
          procesarRespuestaExitosa(response.body());
        } else {
          procesarRespuestaFallida("Error al cargar los pagos: " + response.message());
        }
      }

      @Override
      public void onFailure(Call<List<Pago>> call, Throwable t) {
        procesarRespuestaFallida("Error del servidor: " + t.getMessage());
      }
    });
  }

  private void procesarRespuestaFallida(String error) {
    ocultarEstadoCargando();
    publicarError(error);
    mostrarMensajeListaVacia(); // Mostrar "vacio" en lugar de una pantalla en blanco
  }

  private boolean esRespuestaExitosa(Response<List<Pago>> response) {
    return response.isSuccessful() && response.body() != null;
  }

  private void procesarRespuestaExitosa(List<Pago> pagos) {
    if (pagos.isEmpty()) {
      mostrarMensajeListaVacia();
    } else {
      mostrarListaConDatos(pagos);
    }
  }

  private void mostrarEstadoCargando() {
    mCargando.postValue(View.VISIBLE);
    mVisibilidadRecyclerView.postValue(View.GONE);
    mVisibilidadMensajeVacio.postValue(View.GONE);
  }

  private void ocultarEstadoCargando() {
    mCargando.postValue(View.GONE);
  }

  private void mostrarMensajeListaVacia() {
    mVisibilidadMensajeVacio.postValue(View.VISIBLE);
    mVisibilidadRecyclerView.postValue(View.GONE);
    mListaPagos.postValue(new ArrayList<>());
  }

  private void mostrarListaConDatos(List<Pago> pagos) {
    mVisibilidadMensajeVacio.postValue(View.GONE);
    mVisibilidadRecyclerView.postValue(View.VISIBLE);
    mListaPagos.postValue(pagos);
  }

  private void publicarError(String mensaje) {
    if (mensaje != null && !mensaje.trim().isEmpty()) {
      mError.postValue(mensaje);
    }
  }
}