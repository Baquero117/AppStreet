package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Categoria
import com.example.appinterface.R

class CategoriaAdapter(
    var categorias: List<Categoria>,
    private val onEditar: (Categoria) -> Unit,
    private val onEliminar: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categorias[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = categorias.size


    fun actualizarLista(nuevaLista: List<Categoria>) {
        categorias = nuevaLista
        notifyDataSetChanged()
    }

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtIdCategoria: TextView = itemView.findViewById(R.id.txtid_categoria)
        private val txtNombreCategoria: TextView = itemView.findViewById(R.id.txtNombreCategoria)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarCategoria)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarCategoria)

        fun bind(categoria: Categoria, onEditar: (Categoria) -> Unit, onEliminar: (Categoria) -> Unit) {

            txtIdCategoria.text = "ID: ${categoria.id_categoria}"
            txtNombreCategoria.text = "Nombre: ${categoria.nombre}"

            btnEditar.setOnClickListener {
                onEditar(categoria)
            }

            btnEliminar.setOnClickListener {
                onEliminar(categoria)
            }
        }
    }
}
