//ui/main/inmueble/DetalleInmuebleFragment
package com.example.inmobiliaria.ui.main.inmueble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.request.ApiClient;
import com.example.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;

public class DetalleInmuebleFragment extends Fragment {

  private FragmentDetalleInmuebleBinding binding;
  private DetalleInmuebleViewModel vm;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vm = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
    vm.setInmuebleDesdeBundle(getArguments());

    observarViewModel();
    configurarEventos();
  }

  private void observarViewModel() {
    //Observa los datos del inmueble
    vm.getInmueble().observe(getViewLifecycleOwner(), i -> {
      binding.tvIdInmueble.setText(String.valueOf(i.getIdInmueble()));
      binding.tvDireccionI.setText(i.getDireccion());
      binding.tvUsoI.setText(i.getUso());
      binding.tvAmbientesI.setText(String.valueOf(i.getAmbientes()));
      binding.tvLatitudI.setText(String.valueOf(i.getLatitud()));
      binding.tvLongitudI.setText(String.valueOf(i.getLongitud()));
      binding.tvValorI.setText("$ " + i.getValor());
      binding.checkDisponible.setChecked(i.isDisponible());

      Glide.with(requireContext())
              .load(ApiClient.URL_BASE_AZURE + i.getImagen())
              .placeholder(R.drawable.house)
              .into(binding.imgFotoInmueble);
      vm.marcarCargaCompleta();
    });

    //Observa mensajes del ViewModel (éxitos o errores)
    vm.getMensaje().observe(getViewLifecycleOwner(),
            m -> Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show());
  }

  private void configurarEventos() {
    binding.checkDisponible.setOnCheckedChangeListener((buttonView, isChecked) ->
            vm.actualizarDisponibilidad(isChecked)
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
