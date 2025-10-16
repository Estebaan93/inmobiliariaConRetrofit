//ui/main/PerfilFragment/PerfilFragment
package com.example.inmobiliaria.ui.main.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.inmobiliaria.databinding.FragmentGalleryBinding;
import com.example.inmobiliaria.data.model.Propietario;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {
  private FragmentPerfilBinding binding;
  private PerfilViewModel mv;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     mv= new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

    binding = FragmentPerfilBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    //Observamos datos del viewModel
    mv.getMPropietario().observe(getViewLifecycleOwner(), p ->{
      binding.eTPerfilDni.setText(p.getDni());
      binding.eTPerfilNombre.setText(p.getNombre());
      binding.eTPerfilApellido.setText(p.getApellido());
      binding.eTPerfilEmail.setText(p.getEmail());
      binding.eTPerfilTel.setText(p.getTelefono());
    });

    //Observa habilitacion del campo
    mv.getMEditable().observe(getViewLifecycleOwner(), habilitar -> {
      binding.eTPerfilDni.setEnabled(habilitar);
      binding.eTPerfilNombre.setEnabled(habilitar);
      binding.eTPerfilApellido.setEnabled(habilitar);
      binding.eTPerfilEmail.setEnabled(habilitar);
      binding.eTPerfilTel.setEnabled(habilitar);
    });

    mv.getMBtnTx().observe(getViewLifecycleOwner(), binding.btnEditarActualizarPerfil::setText);

    mv.getMmensaje().observe(getViewLifecycleOwner(), mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    );


    //Accion del boton
    binding.btnEditarActualizarPerfil.setOnClickListener(v ->
            mv.actualizarDatosDesdeVista(
                    binding.eTPerfilDni.getText().toString(),
                    binding.eTPerfilNombre.getText().toString(),
                    binding.eTPerfilApellido.getText().toString(),
                    binding.eTPerfilEmail.getText().toString(),
                    binding.eTPerfilTel.getText().toString()
            )
    );

    //Boton Cambiar Contraseña
    binding.btnCambiarPassword.setOnClickListener(v -> {
      CambiarPasswordDialog dialog = new CambiarPasswordDialog();
      dialog.show(getParentFragmentManager(), "CambiarPasswordDialog");
    });

    mv.cargarPerfil();

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