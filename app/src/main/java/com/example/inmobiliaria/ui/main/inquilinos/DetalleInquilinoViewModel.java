package com.example.inmobiliaria.ui.main.inquilinos;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Contrato;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.data.repositorio.InquilinoRepositorio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<Inquilino> mInquilino = new MutableLiveData<>();

  //nuevo03
  private final MutableLiveData<Contrato> mContrato = new MutableLiveData<>();
  private final MutableLiveData<Contrato> mMostrarDialogoContrato = new MutableLiveData<>();


  private final InquilinoRepositorio repo;
  public DetalleInquilinoViewModel(@NonNull Application application) {
    super(application);
    repo= new InquilinoRepositorio(application);
  }

  public LiveData<String> getMError() {
    return mError;
  }

  //nuevo03
  public LiveData<Contrato> getMContrato() { return mContrato; }
  public LiveData<Contrato> getMostrarDialogoContrato() { return mMostrarDialogoContrato; }

  public LiveData<Inquilino> getMInquilino() {
    return mInquilino;
  }
  public void cargarInquilino(Bundle b) {
    // Validar Bundle
    if (b == null) {
      mError.setValue("Error: No se recibio informacion");
      return;
    }
    Inmueble inm = (Inmueble) b.getSerializable("inmueble");

    if (inm == null) {
      mError.setValue("Error al cargar el inmueble");
      return;
    }
    repo.obtenerContratoPorInmueble(inm.getIdInmueble(), new Callback<Contrato>() {
      @Override
      public void onResponse(Call<Contrato> call, Response<Contrato> response) {
        if (response.isSuccessful() && response.body() != null) {
          Contrato contrato = response.body();
          Inquilino inquilino = contrato.getInquilino();

          if (inquilino != null) {
            mInquilino.setValue(inquilino);
            mContrato.setValue(contrato); //nuevo03
          } else {
            mError.setValue("El contrato no tiene inquilino asociado");
          }
        } else if (response.code() == 404) {
          mError.setValue("Inmueble sin inquilino");
        } else {
          mError.setValue("Error al cargar el inquilino");
        }
      }

      @Override
      public void onFailure(Call<Contrato> call, Throwable t) {
        mError.setValue("Error del servidor");
      }
    });
  }

  //nuevo03
  public void onVerContratoClick() {
    // El ViewModel se lo pasa directamente al Fragment
    mMostrarDialogoContrato.setValue(mContrato.getValue());
  }

}