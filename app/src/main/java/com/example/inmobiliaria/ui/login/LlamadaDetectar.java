//ui/login/LlamadaDetectar
package com.example.inmobiliaria.ui.login;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LlamadaDetectar implements SensorEventListener {
  private OnShakeListener listener;
  private long lastShakeTime = 0;

  public interface OnShakeListener {
    void onShake();
  }

  public void setOnShakeListener(OnShakeListener listener) {
    this.listener = listener;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    float x = event.values[0];
    float y = event.values[1];
    float z = event.values[2];

    float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

    if (acceleration > 15.0f && System.currentTimeMillis() - lastShakeTime > 500) {
      lastShakeTime = System.currentTimeMillis();
      if (listener != null) listener.onShake();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}