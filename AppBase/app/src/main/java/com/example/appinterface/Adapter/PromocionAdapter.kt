package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Promocion

class PromocionAdapter(private val promociones: List<Promocion>) : RecyclerView.Adapter<PromocionAdapter.PromocionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromocionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return PromocionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromocionViewHolder, position: Int) {
        holder.bind(promociones[position])
    }

    override fun getItemCount(): Int = promociones.size

    class PromocionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(promocion: Promocion) {
            (itemView as TextView).text =
                "ID: ${promocion.id_promocion} | Descripción: ${promocion.descripcion} | Descuento: ${promocion.descuento} | " +
                        "Inicio: ${promocion.fecha_inicio} | Fin: ${promocion.fecha_fin} | Producto: ${promocion.id_producto}"
        }
    }
}
