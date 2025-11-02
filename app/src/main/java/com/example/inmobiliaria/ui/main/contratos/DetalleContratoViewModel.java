//ui/main/contratos/DetalleContratoViewModel
package com.example.inmobiliaria.ui.main.contratos;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.ContratoRepositorio;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {
  private final MutableLiveData<String> mContratoId = new MutableLiveData<>();
  private final MutableLiveData<String> mFechaInicio = new MutableLiveData<>();
  private final MutableLiveData<String> mFechaFin = new MutableLiveData<>();
  private final MutableLiveData<String> mMonto = new MutableLiveData<>();
  private final MutableLiveData<String> mInquilinoNombre = new MutableLiveData<>();
  private final MutableLiveData<String> mInmuebleDireccion = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<Integer> mVisibilidadCargando = new MutableLiveData<>();
  private final MutableLiveData<Bundle> mEventoNavegar = new MutableLiveData<>();
  private final ContratoRepositorio repo;
  private Contrato contratoActual; // Guardamos el contrato para la navegacion

  public DetalleContratoViewModel(@NonNull Application application) {
    super(application);
    repo = new ContratoRepositorio(application);
  }

  // Getters para los LiveData de la UI
  public LiveData<String> getMContratoId() {
    return mContratoId;
  }
  public LiveData<String> getMFechaInicio() {
    return mFechaInicio;
  }
  public LiveData<String> getMFechaFin() {
    return mFechaFin;
  }
  public LiveData<String> getMMonto() {
    return mMonto;
  }
  public LiveData<String> getMInquilinoNombre() {
    return mInquilinoNombre;
  }
  public LiveData<String> getMInmuebleDireccion() {
    return mInmuebleDireccion;
  }
  // Getters para estados
  public LiveData<String> getMError() {
    return mError;
  }
  public LiveData<Integer> getMVisibilidadCargando() {
    return mVisibilidadCargando;
  }
  // Getter para el evento de navegacion
  public LiveData<Bundle> getMEventoNavegar() {
    return mEventoNavegar;
  }
  public void cargarContrato(Bundle b) {
    mVisibilidadCargando.setValue(View.VISIBLE);

    // Validacion del Bundle
    if (!validarBundle(b)) {
      return;
    }

    // Extraer el inmueble del Bundle
    Inmueble inm = (Inmueble) b.getSerializable("inmueble");
    if (!validarInmueble(inm)) {
      return;
    }

    // Realizar la peticion al repositorio
    obtenerContratoDesdeRepositorio(inm.getIdInmueble());
  }

  private boolean validarBundle(Bundle b) {
    if (b == null) {
      publicarError("Error: No se recibio informacion del inmueble");
      mVisibilidadCargando.setValue(View.GONE);
      return false;
    }
    return true;
  }

  private boolean validarInmueble(Inmueble inm) {
    if (inm == null) {
      publicarError("Error al cargar el inmueble desde el bundle");
      mVisibilidadCargando.setValue(View.GONE);
      return false;
    }
    return true;
  }

  private void obtenerContratoDesdeRepositorio(int idInmueble) {
    repo.obtenerContratoPorInmueble(idInmueble, new Callback<Contrato>() {
      @Override
      public void onResponse(Call<Contrato> call, Response<Contrato> response) {
        mVisibilidadCargando.postValue(View.GONE);

        if (esRespuestaExitosa(response)) {
          procesarContratoExitoso(response.body());
        } else if (esErrorNoEncontrado(response)) {
          publicarError("El inmueble no tiene un contrato asociado");
        } else {
          publicarError("Error al cargar el contrato: " + response.message());
        }
      }

      @Override
      public void onFailure(Call<Contrato> call, Throwable t) {
        mVisibilidadCargando.postValue(View.GONE);
        publicarError("Error del servidor: " + t.getMessage());
      }
    });
  }

  private boolean esRespuestaExitosa(Response<Contrato> response) {
    return response.isSuccessful() && response.body() != null;
  }

  private boolean esErrorNoEncontrado(Response<Contrato> response) {
    return response.code() == 404;
  }

  private void procesarContratoExitoso(Contrato contrato) {
    contratoActual = contrato;

    // Publicar datos basicos
    mContratoId.setValue(String.valueOf(contrato.getIdContrato()));
    mFechaInicio.setValue(contrato.getFechaInicio());
    mFechaFin.setValue(contrato.getFechaFinalizacion());

    // Formatear y publicar monto
    mMonto.setValue(formatearMoneda(contrato.getMontoAlquiler()));

    // Procesar y publicar informacion del inquilino
    mInquilinoNombre.setValue(procesarInformacionInquilino(contrato));

    // Procesar y publicar informacion del inmueble
    mInmuebleDireccion.setValue(procesarInformacionInmueble(contrato));
  }

  private String formatearMoneda(double monto) {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    return currencyFormatter.format(monto);
  }

  private String procesarInformacionInquilino(Contrato contrato) {
    if (contrato.getInquilino() != null) {
      return contrato.getInquilino().getNombre() + " " +
              contrato.getInquilino().getApellido() +
              " (DNI: " + contrato.getInquilino().getDni() + ")";
    }
    return "Inquilino no disponible";
  }

  private String procesarInformacionInmueble(Contrato contrato) {
    if (contrato.getInmueble() != null) {
      return contrato.getInmueble().getDireccion();
    }
    return "Inmueble no disponible";
  }

  public void navegarAPagos() {
    if (validarContratoParaNavegacion()) {
      Bundle bundle = crearBundleNavegacion();
      // Solo publica si el bundle fue creado exitosamente
      if (bundle != null) {
        mEventoNavegar.setValue(bundle);
      }
    }
  }
  private boolean validarContratoParaNavegacion() {
    if (contratoActual == null) {
      publicarError("Error: No se ha cargado el contrato");
      return false;
    }
    return true;
  }
  private Bundle crearBundleNavegacion() {
    Bundle bundle = new Bundle();
    bundle.putInt("contratoId", contratoActual.getIdContrato());
    return bundle;
  }
  public void finNavegacion() {
    mEventoNavegar.setValue(null);
  }
  private void publicarError(String mensaje) {
    if (mensaje != null && !mensaje.trim().isEmpty()) {
      mError.postValue(mensaje);
    }
  }

}
