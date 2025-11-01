//ui/main/inmueble/CargarInmuebleViewModel
package com.example.inmobiliaria.ui.main.inmueble;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.repositorio.InmuebleRepositorio;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CargarInmuebleViewModel extends AndroidViewModel {
  private final MutableLiveData<Uri> mUriImagen = new MutableLiveData<>();
  private final MutableLiveData<String> mMensaje = new MutableLiveData<>();
  private final MutableLiveData<Integer> mColorTexto = new MutableLiveData<>();
  private final MutableLiveData<Integer> mColorFondo = new MutableLiveData<>();
  private final MutableLiveData<Integer> mVisibilidad = new MutableLiveData<>();
  private final InmuebleRepositorio repo;

  public CargarInmuebleViewModel(@NonNull Application application) {
    super(application);
    repo = new InmuebleRepositorio(application);
    ocultarMensaje();
  }

  public LiveData<Uri> getUriImagen() { return mUriImagen; }
  public LiveData<String> getMensaje() { return mMensaje; }
  public LiveData<Integer> getColorTexto() { return mColorTexto; }
  public LiveData<Integer> getColorFondo() { return mColorFondo; }
  public LiveData<Integer> getVisibilidad() { return mVisibilidad; }

  public void recibirFoto(ActivityResult result) {
    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
      mUriImagen.setValue(result.getData().getData());
      ocultarMensaje();
    }
  }

  public void cargarInmueble(String direccion, String valor, String tipo, String uso,
                             String ambientes, String superficie, String latitud,
                             String longitud, boolean disponible) {
    ocultarMensaje();

    if (direccion.isEmpty() || valor.isEmpty() || tipo.isEmpty() || uso.isEmpty()
            || ambientes.isEmpty() || superficie.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
      error("Debe completar todos los campos");
      return;
    }

    if (mUriImagen.getValue() == null) {
      error("Debe seleccionar una imagen");
      return;
    }

    try {
      double precio = Double.parseDouble(valor);
      if (precio <= 0) { error("El valor debe ser mayor a 0"); return; }

      int amb = Integer.parseInt(ambientes);
      if (amb <= 0 || amb > 20) { error("Ambientes: entre 1 y 20"); return; }

      int sup = Integer.parseInt(superficie);
      if (sup <= 0 || sup > 100000) { error("Superficie: entre 1 y 100000 m2"); return; }

      double lat = Double.parseDouble(latitud);
      if (lat < -90 || lat > 90) { error("Latitud: entre -90 y 90"); return; }

      double lon = Double.parseDouble(longitud);
      if (lon < -180 || lon > 180) { error("Longitud: entre -180 y 180"); return; }

      Inmueble inmu = new Inmueble();
      inmu.setDireccion(direccion);
      inmu.setValor(precio);
      inmu.setTipo(tipo);
      inmu.setUso(uso);
      inmu.setAmbientes(amb);
      inmu.setSuperficie(sup);
      inmu.setLatitud(lat);
      inmu.setLongitud(lon);
      inmu.setDisponible(disponible);

      byte[] imagen = transformarImagen();
      if (imagen.length == 0) { error("Error al procesar imagen"); return; }

      repo.cargarInmueble(inmu, imagen, crearCallback());

    } catch (NumberFormatException e) {
      error("Formato de numero invalido");
    }
  }

  private MutableLiveData<String> crearCallback() {
    MutableLiveData<String> callback = new MutableLiveData<>();
    callback.observeForever(m -> {
      if (m != null) {
        if (m.contains("correctamente")) exito(m);
        else error(m);
      }
    });
    return callback;
  }

  private byte[] transformarImagen() {
    try {
      InputStream is = getApplication().getContentResolver().openInputStream(mUriImagen.getValue());
      Bitmap bmp = BitmapFactory.decodeStream(is);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
      return baos.toByteArray();
    } catch (FileNotFoundException e) {
      return new byte[]{};
    }
  }

  private void error(String msg) {
    mMensaje.setValue(msg);
    mColorTexto.setValue(0xFFD32F2F);
    mColorFondo.setValue(0xFFFFEBEE);
    mVisibilidad.setValue(View.VISIBLE);
  }

  private void exito(String msg) {
    mMensaje.setValue(msg);
    mColorTexto.setValue(0xFF2E7D32);
    mColorFondo.setValue(0xFFE8F5E9);
    mVisibilidad.setValue(View.VISIBLE);
  }

  private void ocultarMensaje() {
    mVisibilidad.setValue(View.GONE);
  }
}