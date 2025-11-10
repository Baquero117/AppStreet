package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Detalle_Producto

class DetalleProductoAdapter(private val detalles: List<Detalle_Producto>) : RecyclerView.Adapter<DetalleProductoAdapter.DetalleProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return DetalleProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetalleProductoViewHolder, position: Int) {
        holder.bind(detalles[position])
    }

    override fun getItemCount(): Int = detalles.size

    class DetalleProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(detalle: Detalle_Producto) {
            (itemView as TextView).text =
                "ID: ${detalle.id_detalle_producto} | Talla: ${detalle.talla} | Color: ${detalle.color} | " +
                        "Imagen: ${detalle.imagen} | Producto: ${detalle.id_producto} | " +
                        "Categoría: ${detalle.id_categoria} | Precio: ${detalle.precio}"
        }
    }
}
