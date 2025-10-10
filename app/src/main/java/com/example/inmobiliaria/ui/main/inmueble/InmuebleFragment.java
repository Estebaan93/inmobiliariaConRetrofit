//ui/main/inmueble/InmuebleFragment
package com.example.inmobiliaria.ui.main.inmueble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;

//import com.example.inmobiliaria.databinding.FragmentSlideshowBinding;

public class InmuebleFragment extends Fragment {
  private FragmentInmuebleBinding binding;
  private InmuebleViewModel mv;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mv = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);

    binding = FragmentInmuebleBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    //final TextView textView = binding.textSlideshow;
    //mv.getText().observe(getViewLifecycleOwner(), textView::setText);
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}