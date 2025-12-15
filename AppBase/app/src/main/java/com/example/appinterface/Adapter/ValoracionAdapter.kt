package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Valoracion
import com.example.appinterface.R

class ValoracionAdapter(
    var valoraciones: List<Valoracion>,
    private val onEditar: (Valoracion) -> Unit,
    private val onEliminar: (Valoracion) -> Unit
) : RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValoracionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_valoracion, parent, false)
        return ValoracionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ValoracionViewHolder, position: Int) {
        holder.bind(valoraciones[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = valoraciones.size


    fun actualizarLista(nuevaLista: List<Valoracion>) {
        valoraciones = nuevaLista
        notifyDataSetChanged()
    }

    class ValoracionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtIdValoracion: TextView = itemView.findViewById(R.id.txtIdValoracion)
        private val txtIdCliente: TextView = itemView.findViewById(R.id.txtIdCliente)
        private val txtIdProducto: TextView = itemView.findViewById(R.id.txtIdProducto)
        private val txtCalificacion: TextView = itemView.findViewById(R.id.txtCalificacion)
        private val txtComentario: TextView = itemView.findViewById(R.id.txtComentario)
        private val txtFecha: TextView = itemView.findViewById(R.id.txtFechaValoracion)


        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarValoracion)

        fun bind(
            valoracion: Valoracion,
            onEditar: (Valoracion) -> Unit,
            onEliminar: (Valoracion) -> Unit
        ) {

            txtIdValoracion.text = "ID: ${valoracion.id_valoracion}"
            txtIdCliente.text = "Cliente ID: ${valoracion.id_cliente}"
            txtIdProducto.text = "Producto ID: ${valoracion.id_producto}"
            txtCalificacion.text = "Calificación: ${valoracion.calificacion}"
            txtComentario.text = "Comentario: ${valoracion.comentario}"
            txtFecha.text = "Fecha: ${valoracion.fecha_valoracion}"


            btnEliminar.setOnClickListener { onEliminar(valoracion) }
        }
    }
}
