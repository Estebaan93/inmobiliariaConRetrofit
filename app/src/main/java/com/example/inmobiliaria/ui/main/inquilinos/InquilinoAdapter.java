//ui/main/inquilinos/InquilinoAdapter
package com.example.inmobiliaria.ui.main.inquilinos;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Inmueble;

import com.example.inmobiliaria.data.request.ApiClient;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.ViewHolderInmuebles> {
  private Context context;
  private List<Inmueble> lista;
  private LayoutInflater li;
  private OnInmuebleClickListener listener;
  public InquilinoAdapter(Context context, List<Inmueble> lista, OnInmuebleClickListener listener) {
    this.context = context;
    this.lista = lista;
    this.li = LayoutInflater.from(context);;
    this.listener = listener;
  }

  // El Fragment usara esto para actualizar la lista cuando lleguen los datos
  public void setLista(List<Inmueble> nuevaLista) {
    this.lista = nuevaLista;
    notifyDataSetChanged(); // Notifica al RecyclerView que los datos cambiaron
  }


  @NonNull
  @Override
  public ViewHolderInmuebles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = li.inflate(R.layout.inquilino_card, parent, false);
    return new ViewHolderInmuebles(itemView);
  }

  // Esta es la logica de Vista
  @Override
  public void onBindViewHolder(@NonNull ViewHolderInmuebles holder, int position) {
      Inmueble inmuebleActual = lista.get(position);

      holder.direccion.setText(inmuebleActual.getDireccion());
      holder.foto.setVisibility(View.VISIBLE);
      holder.btnVer.setVisibility(View.VISIBLE);

      // Cargar imagen con Glide
      String imageUrl = inmuebleActual.getImagen().replace("\\", "/");
      String fullUrl = ApiClient.URL_BASE_AZURE + imageUrl;

      Glide.with(context)
              .load(fullUrl)
              .error(R.drawable.house)
              .placeholder(R.drawable.house)
              .into(holder.foto);

      // Click en el boton VER
      holder.btnVer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onVerClick(inmuebleActual);
        }
      });
    }


  @Override
  public int getItemCount() {
    return lista.size();
  }


  /*Referencia a inquilino_card*/
  public class ViewHolderInmuebles  extends RecyclerView.ViewHolder {
    TextView direccion;
    ImageView foto;
    Button btnVer;
    public ViewHolderInmuebles(@NonNull View itemView) {
      super(itemView);
      direccion = itemView.findViewById(R.id.tvInmuebleDireccion);
      foto = itemView.findViewById(R.id.ivInmuebleAlquilado);
      btnVer = itemView.findViewById(R.id.btnVerInquilino);
    }
  }

  public interface OnInmuebleClickListener {
    void onVerClick(Inmueble inmueble);
  }

}
