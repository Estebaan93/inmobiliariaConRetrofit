// ui/main/contratos/DetalleContratoViewModel
package com.example.inmobiliaria.ui.main.contratos;

import android.app.Application;
import android.os.Bundle;
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
  // LiveData para los campos individuales de la UI
  private final MutableLiveData<String> mContratoId = new MutableLiveData<>();
  private final MutableLiveData<String> mFechaInicio = new MutableLiveData<>();
  private final MutableLiveData<String> mFechaFin = new MutableLiveData<>();
  private final MutableLiveData<String> mMonto = new MutableLiveData<>();
  private final MutableLiveData<String> mInquilinoNombre = new MutableLiveData<>();
  private final MutableLiveData<String> mInmuebleDireccion = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();

  // LiveData para el objeto Contrato (para el listener del Fragment)
  private final MutableLiveData<Contrato> mContrato = new MutableLiveData<>();

  private final ContratoRepositorio repo;

  public DetalleContratoViewModel(@NonNull Application application) {
    super(application);
    repo = new ContratoRepositorio(application);
  }

  // Getters para la UI
  public LiveData<String> getMContratoId() { return mContratoId; }
  public LiveData<String> getMFechaInicio() { return mFechaInicio; }
  public LiveData<String> getMFechaFin() { return mFechaFin; }
  public LiveData<String> getMMonto() { return mMonto; }
  public LiveData<String> getMInquilinoNombre() { return mInquilinoNombre; }
  public LiveData<String> getMInmuebleDireccion() { return mInmuebleDireccion; }
  public LiveData<String> getMError() { return mError; }

  // Getter para el Contrato (usado por el Fragment para el listener)
  public LiveData<Contrato> getMContrato() { return mContrato; }


  public void cargarContrato(Bundle b) {
    if (!validarBundle(b)) return;
    Inmueble inm = (Inmueble) b.getSerializable("inmueble");
    if (!validarInmueble(inm)) return;

    obtenerContratoDesdeRepositorio(inm.getIdInmueble());
  }

  private boolean validarBundle(Bundle b) {
    if (b == null) {
      publicarError("Error: No se recibio informacion del inmueble");
      return false;
    }
    return true;
  }

  private boolean validarInmueble(Inmueble inm) {
    if (inm == null) {
      publicarError("Error al cargar el inmueble desde el bundle");
      return false;
    }
    return true;
  }

  private void obtenerContratoDesdeRepositorio(int idInmueble) {
    repo.obtenerContratoPorInmueble(idInmueble, new Callback<Contrato>() {
      @Override
      public void onResponse(Call<Contrato> call, Response<Contrato> response) {
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
        publicarError("Error del servidor: " + t.getMessage());
      }
    });
  }

  private boolean esRespuestaExitosa(Response<Contrato> response) {
    // Asegura que la respuesta es exitosa Y tiene un cuerpo
    return response.isSuccessful() && response.body() != null;
  }

  private boolean esErrorNoEncontrado(Response<Contrato> response) {
    return response.code() == 404;
  }

  private void procesarContratoExitoso(Contrato contrato) {
    // Publica el objeto Contrato (nunca sera null aquí)
    mContrato.setValue(contrato);

    // Publica los strings individuales para la UI
    mContratoId.setValue(String.valueOf(contrato.getIdContrato()));
    mFechaInicio.setValue(contrato.getFechaInicio());
    mFechaFin.setValue(contrato.getFechaFinalizacion());
    mMonto.setValue(formatearMoneda(contrato.getMontoAlquiler()));
    mInquilinoNombre.setValue(procesarInformacionInquilino(contrato));
    mInmuebleDireccion.setValue(procesarInformacionInmueble(contrato));
  }

  private String formatearMoneda(double monto) {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    return currencyFormatter.format(monto);
  }

  private String procesarInformacionInquilino(Contrato contrato) {
    if (contrato.getInquilino() != null) {
      return String.format("%s %s (DNI: %s)",
              contrato.getInquilino().getNombre(),
              contrato.getInquilino().getApellido(),
              contrato.getInquilino().getDni());
    }
    return "Inquilino no disponible";
  }

  private String procesarInformacionInmueble(Contrato contrato) {
    if (contrato.getInmueble() != null) {
      return contrato.getInmueble().getDireccion();
    }
    return "Inmueble no disponible";
  }

  private void publicarError(String mensaje) {
    if (mensaje != null && !mensaje.trim().isEmpty()) {
      mError.postValue(mensaje);
    }
  }
}