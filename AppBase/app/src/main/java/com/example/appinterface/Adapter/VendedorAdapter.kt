package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Vendedor

class VendedorAdapter(private val vendedores: List<Vendedor>) : RecyclerView.Adapter<VendedorAdapter.VendedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendedorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return VendedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendedorViewHolder, position: Int) {
        holder.bind(vendedores[position])
    }

    override fun getItemCount(): Int = vendedores.size

    class VendedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vendedor: Vendedor) {
            (itemView as TextView).text =
                "ID: ${vendedor.id_vendedor} | Nombre: ${vendedor.nombre} ${vendedor.apellido} | " +
                        "Correo: ${vendedor.correo_electronico} | Teléfono: ${vendedor.telefono} | Contraseña: ${vendedor.contrasena}"
        }
    }
}
