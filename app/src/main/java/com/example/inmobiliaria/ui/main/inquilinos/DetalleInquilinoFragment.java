//ui/main/inquilinos/DetalleInquilinoFragment
package com.example.inmobiliaria.ui.main.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.databinding.FragmentDetalleInquilinoBinding;

public class DetalleInquilinoFragment extends Fragment {

  private DetalleInquilinoViewModel vm;
  private FragmentDetalleInquilinoBinding binding;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    vm = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);
    binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    // Observer para errores
    vm.getMError().observe(getViewLifecycleOwner(), error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    );

    // Observer para el inquilino
    vm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
      @Override
      public void onChanged(Inquilino inquilino) {
        //setear datos
        binding.tvCodigo.setText(String.valueOf(inquilino.getIdInquilino()));
        binding.tvNombre.setText(inquilino.getNombre());
        binding.tvApellido.setText(inquilino.getApellido());
        binding.tvDNI.setText(inquilino.getDni());
        binding.tvEmail.setText(inquilino.getEmail());
        binding.tvTelefono.setText(inquilino.getTelefono());
      }
    });

    //nuevo03
    // Observer para mostrar el dialogo
    vm.getMostrarDialogoContrato().observe(getViewLifecycleOwner(), this::mostrarDialogoContrato);

    // Configurar el botn Ver Contrato - solo llama al metodo del ViewModel
    binding.btnVerContrato.setOnClickListener(v -> vm.onVerContratoClick());



    // Cargar el inquilino con el Bundle recibido
    vm.cargarInquilino(getArguments());

    return root;
  }

  //nuevo03
  private void mostrarDialogoContrato(com.example.inmobiliaria.data.model.Contrato contrato) {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_contrato_detalle, null);
    builder.setView(dialogView);

    AlertDialog dialog = builder.create();

    // Referenciar vistas del dialogo
    android.widget.TextView tvIdContrato = dialogView.findViewById(R.id.tvIdContrato);
    android.widget.TextView tvFechaInicio = dialogView.findViewById(R.id.tvFechaInicio);
    android.widget.TextView tvFechaFin = dialogView.findViewById(R.id.tvFechaFin);
    android.widget.TextView tvMontoAlquiler = dialogView.findViewById(R.id.tvMontoAlquiler);
    android.widget.TextView tvEstadoContrato = dialogView.findViewById(R.id.tvEstadoContrato);
    android.widget.Button btnVerPagos = dialogView.findViewById(R.id.btnVerPagos);
    android.widget.Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);

    // Setear datos del contrato
    tvIdContrato.setText(String.valueOf(contrato.getIdContrato()));
    tvFechaInicio.setText(contrato.getFechaInicio());
    tvFechaFin.setText(contrato.getFechaFinalizacion());

    // Formatear monto
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "AR"));
    tvMontoAlquiler.setText(currencyFormatter.format(contrato.getMontoAlquiler()));

    tvEstadoContrato.setText(contrato.isEstado() ? "Vigente" : "Finalizado");

    // Configurar boton Ver Pagos
    btnVerPagos.setOnClickListener(v -> {
      dialog.dismiss();
      navegarAPagos(contrato.getIdContrato());
    });

    // Configurar boton Cerrar
    btnCerrar.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private void navegarAPagos(int idContrato) {
    Bundle bundle = new Bundle();
    bundle.putInt("contratoId", idContrato);
    Navigation.findNavController(requireView()).navigate(R.id.pagosFragment, bundle);
  }




  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }



}