//ui/main/inicio/InicioViewModel
package com.example.inmobiliaria.ui.main.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class InicioViewModel extends AndroidViewModel {

  private MutableLiveData<MapaActual> mMapaActual;

  public InicioViewModel(@NonNull Application application) {
    super(application);

  }

  public LiveData<MapaActual>getMMapaActual(){
    if(mMapaActual==null){
      mMapaActual= new MutableLiveData<>();
    }
    return mMapaActual;
  }

  public void cargarMapa(){
    MapaActual mapaActual= new MapaActual();
    mMapaActual.setValue(mapaActual);
  }

  public void inicializarMapa(SupportMapFragment mapFragment, LifecycleOwner owner) {
    getMMapaActual().observe(owner, mapaActual -> mapFragment.getMapAsync(mapaActual));
    cargarMapa();
  }

  public class MapaActual implements OnMapReadyCallback{
    LatLng sanLuis = new LatLng(-33.280576, -66.332482);
    LatLng ulp = new LatLng(-33.150720, -66.306864);
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
      MarkerOptions marcadorSanLuis= new MarkerOptions();
      marcadorSanLuis.position(sanLuis);
      //Agregamos el titulo
      marcadorSanLuis.title("San Luis");

      MarkerOptions marcadorULP= new MarkerOptions();
      marcadorULP.position(ulp);
      marcadorULP.title("ULP");


      //Agregamos al mapa
      googleMap.addMarker(marcadorSanLuis);
      googleMap.addMarker(marcadorULP);

      //Recibe un int
      googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

      //Posicion inical
      CameraPosition cameraPosition= new CameraPosition
              .Builder()
              .target(sanLuis)
              .zoom(30)
              .bearing(45)
              .tilt(15)
              .build();
      CameraUpdate cameraUpdate= CameraUpdateFactory.newCameraPosition(cameraPosition);

      googleMap.animateCamera(cameraUpdate);

    }


  }


}