package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Promocion
import com.example.appinterface.R

class PromocionAdapter(
    var promociones: List<Promocion>,
    private val onEditar: (Promocion) -> Unit,
    private val onEliminar: (Promocion) -> Unit
) : RecyclerView.Adapter<PromocionAdapter.PromocionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromocionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promocion, parent, false)
        return PromocionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromocionViewHolder, position: Int) {
        holder.bind(promociones[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = promociones.size


    fun actualizarLista(nuevaLista: List<Promocion>) {
        promociones = nuevaLista
        notifyDataSetChanged()
    }

    class PromocionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtIdPromocion: TextView = itemView.findViewById(R.id.txtIdPromocion)
        private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        private val txtDescuento: TextView = itemView.findViewById(R.id.txtDescuento)
        private val txtInicio: TextView = itemView.findViewById(R.id.txtFechaInicio)
        private val txtFin: TextView = itemView.findViewById(R.id.txtFechaFin)
        private val txtProducto: TextView = itemView.findViewById(R.id.txtProducto)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarPromocion)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarPromocion)

        fun bind(promocion: Promocion, onEditar: (Promocion) -> Unit, onEliminar: (Promocion) -> Unit) {

            txtIdPromocion.text = "ID: ${promocion.id_promocion}"
            txtDescripcion.text = "Descripción: ${promocion.descripcion}"
            txtDescuento.text = "Descuento: ${promocion.descuento}%"
            txtInicio.text = "Inicio: ${promocion.fecha_inicio}"
            txtFin.text = "Fin: ${promocion.fecha_fin}"
            txtProducto.text = "Producto ID: ${promocion.id_producto}"

            btnEditar.setOnClickListener { onEditar(promocion) }
            btnEliminar.setOnClickListener { onEliminar(promocion) }
        }
    }
}
