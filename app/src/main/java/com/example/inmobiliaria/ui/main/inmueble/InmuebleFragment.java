//ui/main/inmueble/InmuebleFragment
package com.example.inmobiliaria.ui.main.inmueble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;

import java.util.ArrayList;

//import com.example.inmobiliaria.databinding.FragmentSlideshowBinding;

public class InmuebleFragment extends Fragment {
  private FragmentInmuebleBinding binding;
  private InmuebleAdapter adapter;
  private InmuebleViewModel mv;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentInmuebleBinding.inflate(inflater, container, false);

    mv= new ViewModelProvider(this).get(InmuebleViewModel.class);

    configurarRecycler();
    observarViewModel();

    return binding.getRoot();
  }

  private void configurarRecycler() {
    GridLayoutManager gridLayout = new GridLayoutManager(requireContext(), 2);
    binding.itemListaInmuebles.setLayoutManager(gridLayout);
    adapter = new InmuebleAdapter(requireContext(), new ArrayList<>());
    binding.itemListaInmuebles.setAdapter(adapter);
  }

  private void observarViewModel() {
    mv.getListaInmuebles().observe(getViewLifecycleOwner(), adapter::actualizarLista);
    mv.getMensaje().observe(getViewLifecycleOwner(), m ->
            Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}