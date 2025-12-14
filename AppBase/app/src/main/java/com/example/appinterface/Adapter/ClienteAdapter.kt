package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Cliente
import com.example.appinterface.R

class ClienteAdapter(
    var clientes: List<Cliente>,
    private val onEditar: (Cliente) -> Unit,
    private val onEliminar: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.bind(clientes[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = clientes.size


    fun actualizarLista(nuevaLista: List<Cliente>) {
        clientes = nuevaLista
        notifyDataSetChanged()
    }

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtId_cliente: TextView= itemView.findViewById(R.id.txtid_cliente)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        private val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
        private val txtTelefono: TextView = itemView.findViewById(R.id.txtTelefono)
        private val txtDireccion: TextView = itemView.findViewById(R.id.txtDireccion)

        private val btnEditar = itemView.findViewById<Button>(R.id.btnEditar)
        private val btnEliminar = itemView.findViewById<Button>(R.id.btnEliminar)

        fun bind(cliente: Cliente, onEditar: (Cliente) -> Unit, onEliminar: (Cliente) -> Unit) {
            txtId_cliente.text = "${cliente.id_cliente}"
            txtNombre.text = "${cliente.nombre} ${cliente.apellido}"
            txtCorreo.text = "Correo: ${cliente.correo_electronico}"
            txtTelefono.text = "Teléfono: ${cliente.telefono}"
            txtDireccion.text = "Dirección: ${cliente.direccion}"

            btnEditar.setOnClickListener {
                onEditar(cliente)
            }

            btnEliminar.setOnClickListener {
                onEliminar(cliente)
            }
        }
    }
}
