//data/preferencias/SessionManager
package com.example.inmobiliaria.data.preferencias;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
  private static final String PREF_NOMBRE="session_sp";
  private static final String KEY_TOKEN= "token";
  private final SharedPreferences sp;

  public SessionManager(Context context){
    sp = context.getSharedPreferences(PREF_NOMBRE, 0);

  }

  public void guardarToken(String token){
    sp.edit().putString(KEY_TOKEN, token).apply();
  }

  public String leerToken(){
    return sp.getString(KEY_TOKEN, null);
  }

  public void cerrarSesion(){
    sp.edit().clear().apply();
  }

  public boolean sesionActiva(){
    String token= sp.getString(KEY_TOKEN, null);
    return token!= null && !token.isEmpty();
  }

}
