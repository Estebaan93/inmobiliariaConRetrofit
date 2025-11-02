// ui/main/pagos/PagosAdapter
package com.example.inmobiliaria.ui.main.pagos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Pago;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {
  private List<Pago> listaPagos;
  // Optimización: Crear el formateador una sola vez
  private final NumberFormat currencyFormatter;

  public PagosAdapter(List<Pago> listaPagos) {
    this.listaPagos = listaPagos;
    this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
  }

  public void setListaPagos(List<Pago> nuevaLista) {
    this.listaPagos = nuevaLista;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // El LayoutInflater se obtiene del contexto del 'parent'
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.pago_card, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Pago pago = listaPagos.get(position);

    holder.tvIdPago.setText(String.valueOf(pago.getIdPago()));
    holder.tvFechaPago.setText(pago.getFechaPago());
    holder.tvMonto.setText(currencyFormatter.format(pago.getMonto())); // Usar el campo
    holder.tvDetalle.setText(pago.getDetalle() != null ? pago.getDetalle() : "Sin detalle");
    holder.tvEstado.setText(pago.isEstado() ? "Pagado" : "Pendiente");
    holder.tvIdContrato.setText(String.valueOf(pago.getIdContrato()));
  }

  @Override
  public int getItemCount() {
    // El ViewModel se asegura de que la lista nunca sea nula
    return listaPagos.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvIdPago;
    TextView tvFechaPago;
    TextView tvMonto;
    TextView tvDetalle;
    TextView tvEstado;
    TextView tvIdContrato;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvIdPago = itemView.findViewById(R.id.tvIdPago);
      tvFechaPago = itemView.findViewById(R.id.tvFechaPago);
      tvMonto = itemView.findViewById(R.id.tvMonto);
      tvDetalle = itemView.findViewById(R.id.tvDetalle);
      tvEstado = itemView.findViewById(R.id.tvEstado);
      tvIdContrato = itemView.findViewById(R.id.tvIdContrato);
    }
  }
}