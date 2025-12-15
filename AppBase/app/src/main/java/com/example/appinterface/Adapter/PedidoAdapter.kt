package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Pedido
import com.example.appinterface.R

class PedidoAdapter(
    var pedidos: List<Pedido>,
    private val onEditar: (Pedido) -> Unit,
    private val onEliminar: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(pedidos[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = pedidos.size


    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtIdPedido: TextView = itemView.findViewById(R.id.txtIdPedido)
        private val txtIdCliente: TextView = itemView.findViewById(R.id.txtIdCliente)
        private val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        private val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        private val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarPedido)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarPedido)

        fun bind(pedido: Pedido, onEditar: (Pedido) -> Unit, onEliminar: (Pedido) -> Unit) {

            txtIdPedido.text = "ID Pedido: ${pedido.id_pedido}"
            txtIdCliente.text = "Cliente: ${pedido.id_cliente}"
            txtFecha.text = "Fecha: ${pedido.fecha_pedido}"
            txtTotal.text = "Total: ${pedido.total}"
            txtEstado.text = "Estado: ${pedido.estado}"

            btnEditar.setOnClickListener { onEditar(pedido) }
            btnEliminar.setOnClickListener { onEliminar(pedido) }
        }
    }
}
