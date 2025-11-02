//ui/main/contratos/ContratoAdapter
package com.example.inmobiliaria.ui.main.contratos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.request.ApiClient;
import java.util.List;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ViewHolder> {
  private Context context;
  private List<Inmueble> lista;
  private LayoutInflater li;

  public ContratoAdapter(Context context, List<Inmueble> lista) {
    this.context = context;
    this.lista = lista;
    this.li = LayoutInflater.from(context);
  }

  public void setLista(List<Inmueble> nuevaLista) {
    this.lista = nuevaLista;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = li.inflate(R.layout.contrato_card, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Inmueble inmuebleActual = lista.get(position);
    holder.direccion.setText(inmuebleActual.getDireccion());

    // Cargar imagen con Glide
    String imageUrl = inmuebleActual.getImagen().replace("\\", "/");
    String fullUrl = ApiClient.URL_BASE_AZURE + imageUrl;

    Glide.with(context)
            .load(fullUrl)
            .error(R.drawable.house) // Imagen de error si falla la carga
            .placeholder(R.drawable.house) // Imagen placeholder mientras carga
            .into(holder.foto);

    holder.btnVer.setOnClickListener(v -> {
      // El Adapter ahora crea el Bundle y navega
      Bundle bundle = new Bundle();
      bundle.putSerializable("inmueble", inmuebleActual);
      Navigation.findNavController(v)
              .navigate(R.id.action_nav_contrato_to_detalleContratoFragment, bundle);
    });
  }

  @Override
  public int getItemCount() { return lista.size(); }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView direccion;
    ImageView foto;
    Button btnVer;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      direccion = itemView.findViewById(R.id.tvDireccionContrato);
      foto = itemView.findViewById(R.id.ivInmuebleContrato);
      btnVer = itemView.findViewById(R.id.btnVerContrato);
    }
  }

  }