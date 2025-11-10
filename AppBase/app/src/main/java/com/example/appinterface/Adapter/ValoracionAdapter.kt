package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Valoracion

class ValoracionAdapter(private val valoraciones: List<Valoracion>) : RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValoracionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ValoracionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ValoracionViewHolder, position: Int) {
        holder.bind(valoraciones[position])
    }

    override fun getItemCount(): Int = valoraciones.size

    class ValoracionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(valoracion: Valoracion) {
            (itemView as TextView).text =
                "ID: ${valoracion.id_valoracion} | Cliente: ${valoracion.id_cliente} | Producto: ${valoracion.id_producto} | " +
                        "Calificación: ${valoracion.calificacion} | Comentario: ${valoracion.comentario} | Fecha: ${valoracion.fecha_valoracion}"
        }
    }
}
