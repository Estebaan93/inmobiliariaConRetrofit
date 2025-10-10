//ui/main/PerfilFragment/PerfilFragment
package com.example.inmobiliaria.ui.main.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.inmobiliaria.databinding.FragmentGalleryBinding;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {
  private FragmentPerfilBinding binding;
  private PerfilViewModel mv;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     mv= new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

    binding = FragmentPerfilBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    //final TextView textView = binding.textGallery;
    //mv.getText().observe(getViewLifecycleOwner(), textView::setText);
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}