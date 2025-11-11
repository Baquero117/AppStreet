package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Cliente

class ClienteAdapter(private val clientes: List<Cliente>) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.bind(clientes[position])
    }

    override fun getItemCount(): Int = clientes.size

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cliente: Cliente) {
            (itemView as TextView).text =
                "ID: ${cliente.id_cliente} | Nombre: ${cliente.nombre} ${cliente.apellido} | " +
                        "Correo: ${cliente.correo_electronico} | Teléfono: ${cliente.telefono} | " +
                        "Dirección: ${cliente.direccion} | Contraseña: ${cliente.contrasena}"
        }
    }
}
