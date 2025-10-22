//ui/main/inmueble/InmuebleAdapter
package com.example.inmobiliaria.ui.main.inmueble;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Inmueble;
import com.example.inmobiliaria.data.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {

  private Context context;
  private List<Inmueble> lista= new ArrayList<>();

  public InmuebleAdapter(Context context, List<Inmueble> listaInicial) {
    this.context = context;
    if (listaInicial != null)
      this.lista = listaInicial;
    else
      this.lista = new ArrayList<>();
  }

  public void actualizarLista(List<Inmueble> nuevaLista) {
    this.lista = nuevaLista;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.inmueble_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Inmueble i = lista.get(position);
    holder.tvDireccion.setText(i.getDireccion());
    holder.tvUso.setText(i.getUso());
    holder.tvValor.setText("$" + i.getValor());
    Glide.with(context)
            .load(ApiClient.URL_BASE_AZURE + i.getImagen())
            .placeholder(R.drawable.house)
            .into(holder.imgInmueble);


    /*Debemos crear un bundle */
    holder.cardView.setOnClickListener(view -> {
      Bundle bundle = new Bundle();
      bundle.putSerializable("inmueble", i);
      Navigation.findNavController(view)
              .navigate(R.id.detalleInmuebleFragment, bundle);

    });
  }

  @Override
  public int getItemCount() {
    return lista.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvDireccion, tvValor, tvUso;
    ImageView imgInmueble;
    CardView cardView;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDireccion = itemView.findViewById(R.id.tVDireccion);
      tvUso = itemView.findViewById(R.id.tvUso);
      tvValor = itemView.findViewById(R.id.tvValor);
      imgInmueble = itemView.findViewById(R.id.imgInmueble);
      cardView = itemView.findViewById(R.id.idCardView);

    }
  }
}
