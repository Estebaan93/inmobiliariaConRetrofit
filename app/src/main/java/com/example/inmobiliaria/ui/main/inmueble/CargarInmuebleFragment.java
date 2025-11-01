//ui/main/inmueble/CargarInmuebleFragment
package com.example.inmobiliaria.ui.main.inmueble;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentCargarInmuebleBinding;

public class CargarInmuebleFragment extends Fragment {

  private CargarInmuebleViewModel vm;
  private FragmentCargarInmuebleBinding binding;
  private ActivityResultLauncher<Intent> launcher;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentCargarInmuebleBinding.inflate(inflater, container, false);
    vm = new ViewModelProvider(this).get(CargarInmuebleViewModel.class);

    launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> vm.recibirFoto(result)
    );

    vm.getUriImagen().observe(getViewLifecycleOwner(), uri -> binding.imgView.setImageURI(uri));
    vm.getMensaje().observe(getViewLifecycleOwner(), m -> binding.tvMensaje.setText(m));
    vm.getColorTexto().observe(getViewLifecycleOwner(), c -> binding.tvMensaje.setTextColor(c));
    vm.getColorFondo().observe(getViewLifecycleOwner(), c -> binding.tvMensaje.setBackgroundColor(c));
    vm.getVisibilidad().observe(getViewLifecycleOwner(), v -> binding.tvMensaje.setVisibility(v));

    binding.btnFoto.setOnClickListener(v ->
            launcher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    );

    binding.btnCargar.setOnClickListener(v ->
            vm.cargarInmueble(
                    binding.etDireccion.getText().toString(),
                    binding.etValor.getText().toString(),
                    binding.etTipo.getText().toString(),
                    binding.etUso.getText().toString(),
                    binding.etAmbientes.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    binding.etLatitud.getText().toString(),
                    binding.etLongitud.getText().toString(),
                    binding.cbDisp.isChecked()
            )
    );

    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().findViewById(R.id.fab).setVisibility(View.GONE);
  }

  @Override
  public void onPause() {
    super.onPause();
    requireActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}