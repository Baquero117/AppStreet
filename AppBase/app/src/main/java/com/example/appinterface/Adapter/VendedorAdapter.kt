package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Vendedor
import com.example.appinterface.R

class VendedorAdapter(
    var vendedores: List<Vendedor>,
    private val onEditar: (Vendedor) -> Unit,
    private val onEliminar: (Vendedor) -> Unit
) : RecyclerView.Adapter<VendedorAdapter.VendedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendedorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vendedor, parent, false)
        return VendedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendedorViewHolder, position: Int) {
        holder.bind(vendedores[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = vendedores.size


    fun actualizarLista(nuevaLista: List<Vendedor>) {
        vendedores = nuevaLista
        notifyDataSetChanged()
    }

    class VendedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtId: TextView = itemView.findViewById(R.id.txtid_vendedor)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombreVendedor)
        private val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreoVendedor)
        private val txtTelefono: TextView = itemView.findViewById(R.id.txtTelefonoVendedor)
        private val txtContra: TextView = itemView.findViewById(R.id.txtContrasenaVendedor)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarVendedor)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarVendedor)

        fun bind(vendedor: Vendedor, onEditar: (Vendedor) -> Unit, onEliminar: (Vendedor) -> Unit) {

            txtId.text = vendedor.id_vendedor.toString()
            txtNombre.text = "${vendedor.nombre} ${vendedor.apellido}"
            txtCorreo.text = vendedor.correo_electronico
            txtTelefono.text = vendedor.telefono
            txtContra.text = vendedor.contrasena

            btnEditar.setOnClickListener { onEditar(vendedor) }
            btnEliminar.setOnClickListener { onEliminar(vendedor) }
        }
    }
}
