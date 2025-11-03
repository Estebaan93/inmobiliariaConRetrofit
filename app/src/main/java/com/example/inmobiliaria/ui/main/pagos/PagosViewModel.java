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
  private final MutableLiveData<String> mMensajeEnPantalla = new MutableLiveData<>();
  private final MutableLiveData<Integer> mCargando = new MutableLiveData<>(View.GONE);
  private final MutableLiveData<String> mCodigoContrato = new MutableLiveData<>();
  private final MutableLiveData<Integer> mVisibilidadMensaje = new MutableLiveData<>(View.GONE);
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
  public LiveData<String> getMensajeEnPantalla() {
    return mMensajeEnPantalla;
  }
  public LiveData<Integer> getMCargando() {
    return mCargando;
  }
  public LiveData<String> getMCodigoContrato() {
    return mCodigoContrato;
  }
  public LiveData<Integer> getMVisibilidadMensaje() {
    return mVisibilidadMensaje;
  }
  public LiveData<Integer> getMVisibilidadRecyclerView() {
    return mVisibilidadRecyclerView;
  }

  public void cargarPagos(Bundle bundle) {
    if (bundle == null) {
      mostrarMensajeError("Error: No se recibieron datos del contrato");
      return;
    }

    int contratoId = bundle.getInt("contratoId", -1);

    if (contratoId == -1) {
      mostrarMensajeError("Error: No se recibio el ID del contrato");
      return;
    }

    obtenerPagosDesdeRepositorio(contratoId);
  }

  private void obtenerPagosDesdeRepositorio(int contratoId) {
    mostrarEstadoCargando();
    mCodigoContrato.setValue(String.format("Contrato #%d", contratoId));

    repo.obtenerPagosPorContrato(contratoId, new Callback<List<Pago>>() {
      @Override
      public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
        ocultarEstadoCargando();
        if (esRespuestaExitosa(response)) {
          procesarRespuestaExitosa(response.body());
        } else {
          mostrarMensajeError("Error al cargar los pagos");
        }
      }

      @Override
      public void onFailure(Call<List<Pago>> call, Throwable t) {
        ocultarEstadoCargando();
        mostrarMensajeError("Error de conexion. Intente nuevamente");
      }
    });
  }

  private boolean esRespuestaExitosa(Response<List<Pago>> response) {
    return response.isSuccessful() && response.body() != null;
  }

  private void procesarRespuestaExitosa(List<Pago> pagos) {
    if (pagos.isEmpty()) {
      mostrarMensajeVacio();
    } else {
      mostrarListaConDatos(pagos);
    }
  }

  private void mostrarEstadoCargando() {
    mCargando.postValue(View.VISIBLE);
    mVisibilidadRecyclerView.postValue(View.GONE);
    mVisibilidadMensaje.postValue(View.GONE);
  }

  private void ocultarEstadoCargando() {
    mCargando.postValue(View.GONE);
  }

  private void mostrarMensajeVacio() {
    mMensajeEnPantalla.postValue("No hay pagos registrados para este contrato");
    mVisibilidadMensaje.postValue(View.VISIBLE);
    mVisibilidadRecyclerView.postValue(View.GONE);
    mListaPagos.postValue(new ArrayList<>());
  }

  private void mostrarMensajeError(String error) {
    mMensajeEnPantalla.postValue(error);
    mVisibilidadMensaje.postValue(View.VISIBLE);
    mVisibilidadRecyclerView.postValue(View.GONE);
    mListaPagos.postValue(new ArrayList<>());
  }

  private void mostrarListaConDatos(List<Pago> pagos) {
    mVisibilidadMensaje.postValue(View.GONE);
    mVisibilidadRecyclerView.postValue(View.VISIBLE);
    mListaPagos.postValue(pagos);
  }
}