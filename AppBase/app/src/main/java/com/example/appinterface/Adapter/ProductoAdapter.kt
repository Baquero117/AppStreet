package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.DataClass.Producto
import com.example.appinterface.R
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.content.Intent
import com.example.appinterface.ImagenFullscreenActivity



class ProductoAdapter(
    var productos: List<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productos[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = productos.size


    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtId: TextView = itemView.findViewById(R.id.txtIdProducto)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombreProducto)

        private val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)

        private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcionProducto)
        private val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadProducto)
        private val txtEstado: TextView = itemView.findViewById(R.id.txtEstadoProducto)
        private val txtVendedor: TextView = itemView.findViewById(R.id.txtVendedorProducto)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarProducto)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarProducto)

        fun bind(
            producto: Producto,
            onEditar: (Producto) -> Unit,
            onEliminar: (Producto) -> Unit


        ) {
            imgProducto.setOnClickListener {
                val intent = Intent(itemView.context, ImagenFullscreenActivity::class.java)
                intent.putExtra("imagen", producto.imagen)
                itemView.context.startActivity(intent)
            }

            txtId.text = producto.id_producto.toString()
            txtNombre.text = producto.nombre

            if (!producto.imagen.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(producto.imagen)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(imgProducto)
            } else {

                imgProducto.setImageResource(R.drawable.ic_image_placeholder)
            }

            txtDescripcion.text = producto.descripcion
            txtCantidad.text = "Cantidad: ${producto.cantidad}"
            txtEstado.text = "Estado: ${producto.estado}"
            txtVendedor.text = "Vendedor: ${producto.id_vendedor}"

            btnEditar.setOnClickListener { onEditar(producto) }
            btnEliminar.setOnClickListener { onEliminar(producto) }
        }

    }
}
