package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Carrito
class CarritoAdapter(private val carritos: List<Carrito>) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(carritos[position])
    }

    override fun getItemCount(): Int = carritos.size

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(carrito: Carrito) {
            (itemView as TextView).text =
                "ID: ${carrito.id_carrito} | Cliente: ${carrito.id_cliente} | Detalle: ${carrito.id_detalle_producto} | " +
                        "Cant: ${carrito.cantidad} | Precio: ${carrito.precio_unitario} | Subtotal: ${carrito.subtotal}"
        }
    }
}


