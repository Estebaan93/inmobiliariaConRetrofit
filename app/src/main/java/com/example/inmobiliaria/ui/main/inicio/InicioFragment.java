//ui/main/inicio/InicioFragment
package com.example.inmobiliaria.ui.main.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.inmobiliaria.databinding.FragmentHomeBinding;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInicioBinding;
import com.google.android.gms.maps.SupportMapFragment;

public class InicioFragment extends Fragment {
  private FragmentInicioBinding binding;
  private InicioViewModel mv;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    binding = FragmentInicioBinding.inflate(inflater, container, false);

    //mv = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(InicioViewModel.class);
    mv= new ViewModelProvider(requireActivity()).get(InicioViewModel.class);



    // Vincula el mapa directamente; la logica de control está en el ViewModel
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);

    configurarMapa();


    View root = binding.getRoot();
    return root;
  }
  private void configurarMapa() {
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);
    mv.getMapaActual().observe(getViewLifecycleOwner(), mapaActual -> mapFragment.getMapAsync(mapaActual));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}