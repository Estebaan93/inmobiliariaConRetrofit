//ui/main/PerfilFragment/PerfilViewModel
package com.example.inmobiliaria.ui.main.perfil;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.data.model.Propietario;
import com.example.inmobiliaria.data.repositorio.PropietarioRepositorio;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

  private MutableLiveData<String> mBtnTx= new MutableLiveData<>("Editar");
  private MutableLiveData<Propietario> mPropietario= new MutableLiveData<>();
  private MutableLiveData<Boolean> mEditable= new MutableLiveData<>(false);
  private final MutableLiveData<String> mMensaje = new MutableLiveData<>();
  private final MutableLiveData<Boolean> cerrarDialog = new MutableLiveData<>(false);
  private final PropietarioRepositorio repo;

  public PerfilViewModel(@NonNull Application application) {
    super(application);
    repo= new PropietarioRepositorio(application.getApplicationContext());
  }

  //observables
  public LiveData<Propietario> getMPropietario(){
    return mPropietario;
  }
  public LiveData<Boolean> getMEditable(){
    return mEditable;
  }
  public LiveData<String> getMBtnTx(){
    return mBtnTx;
  }
  public LiveData<String> getMmensaje(){
    return mMensaje;
  }
  public LiveData<Boolean> getCerrarDialog(){
  return cerrarDialog;
}

  //Cargar perfil
  public void cargarPerfil(){
    mMensaje.setValue("");
    repo.obtenerPerfil(new Callback<Propietario>() {
      @Override
      public void onResponse(Call<Propietario> call, Response<Propietario> response) {
        if(response.isSuccessful() && response.body() !=null){
          mPropietario.postValue(response.body());
          Log.d("CARGAR_PROPIETARIO: ", "DATOS: "+ new Gson().toJson(response.body()));
        }else {
          Log.d("ERROR_CARGARPROPIETARIO", "Error al obtener el propietario: "+ response.body() +" "+ response.isSuccessful());
          mMensaje.postValue("Error al obtener el perfil");
        }
      }

      @Override
      public void onFailure(Call<Propietario> call, Throwable t) {
        mMensaje.postValue("Error de conexion: " + t.getMessage());
      }
    });
  }

  //Boton principal
  /*public void onBtnPresionado(Propietario nuevo){
    Boolean modoEdicion= mEditable.getValue();
    if(modoEdicion != null && modoEdicion){
      guardarCambios(nuevo);
    }else {
      mEditable.setValue(true);
      mBtnTx.setValue("Guardar");
    }
  }*/
  //Guardar cambios
  private void guardarCambios(Propietario actualizado){
    mMensaje.setValue("");
    repo.actualizarPerfil(actualizado, new Callback<Propietario>() {
      @Override
      public void onResponse(Call<Propietario> call, Response<Propietario> response) {
        if(response.isSuccessful() && response.body() !=null){
          mPropietario.postValue(response.body());
          mEditable.postValue(false);
          mBtnTx.postValue("Editar");
          mMensaje.postValue("Perfil actualizado correctamente");
        }else{
          mMensaje.postValue("Error al actualizar el perfil");
        }

      }

      @Override
      public void onFailure(Call<Propietario> call, Throwable t) {
        Log.d("ERROR_API_ACTUALIZAR", "Error de la API "+ t.getMessage());
        mMensaje.postValue("Error de conexion: " + t.getMessage());
      }
    });
  }

  // Mapea datos desde la vista
  public void actualizarDatosDesdeVista(String dniStr, String nombre, String apellido, String email, String telefono) {
    Boolean modoEdicion = mEditable.getValue();
    if(modoEdicion == null) return;
    if (modoEdicion){
      if (dniStr.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
        mMensaje.postValue("Todos los campos son obligatorios");
        return;
      }
      int dni;
      try{
        dni= Integer.parseInt(dniStr);
        if(dni<0){
          mMensaje.postValue("El DNI no puede ser negativo");
          return;
        }
      }catch (NumberFormatException e){
        mMensaje.postValue("El DNI debe ser un numero");
        return;
      }
      if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        mMensaje.postValue("Ingrese un email valido");
        return;
      }
      Propietario p = new Propietario();
      p.setDni(dni);
      p.setNombre(nombre);
      p.setApellido(apellido);
      p.setEmail(email);
      p.setTelefono(telefono);
      //onBtnPresionado(p);
      guardarCambios(p);
    }else{
      mEditable.setValue(true);
      mBtnTx.setValue("Guardar");
    }
    }


  // Devuelve mensaje actual solo si no está vacío
  /*public String obtenerMensajeVisible() {
    String m = mMensaje.getValue();
    if (m == null || m.isEmpty()) return null;
    return m;
  }*/

  // --- Cambiar contraseña ---
  public void onCambiarPasswordClick(String actual, String nueva, String confirmar) {
    if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
      mMensaje.setValue("Completa todos los campos");
      return;
    }
    if (!nueva.equals(confirmar)) {
      mMensaje.setValue("Las contraseñas no coinciden");
      return;
    }

    cambiarPassword(actual, nueva);
  }

  private void cambiarPassword(String actual, String nueva) {
    repo.cambiarPassword(actual, nueva, new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          mMensaje.postValue("Contraseña actualizada correctamente");
          cerrarDialog.postValue(true); // Para que el diálogo se cierre
        } else {
          mMensaje.postValue("Error al cambiar contraseña (" + response.code() + ")");
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        mMensaje.postValue("Error de conexin: " + t.getMessage());
      }
    });


    }

  }