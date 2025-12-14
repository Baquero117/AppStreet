package com.example.appinterface.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinterface.DataClass.Detalle_Producto
import com.example.appinterface.ImagenFullscreenActivity
import com.example.appinterface.R

class DetalleProductoAdapter(
    private var detalles: List<Detalle_Producto>,
    private val onEditar: (Detalle_Producto) -> Unit,
    private val onEliminar: (Detalle_Producto) -> Unit
) : RecyclerView.Adapter<DetalleProductoAdapter.DetalleProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detalle_producto, parent, false)
        return DetalleProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetalleProductoViewHolder, position: Int) {
        holder.bind(detalles[position], onEditar, onEliminar)
    }

    override fun getItemCount(): Int = detalles.size


    fun actualizarLista(nuevaLista: List<Detalle_Producto>) {
        detalles = nuevaLista
        notifyDataSetChanged()
    }

    class DetalleProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtId: TextView = itemView.findViewById(R.id.txtIdDetalle)
        private val txtTalla: TextView = itemView.findViewById(R.id.txtTalla)
        private val txtColor: TextView = itemView.findViewById(R.id.txtColor)
        private val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        private val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
        private val txtProducto: TextView = itemView.findViewById(R.id.txtProducto)

        private val imgDetalle: ImageView = itemView.findViewById(R.id.imgDetalle)

        private val btnEditar: Button = itemView.findViewById(R.id.btnEditarDetalle)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarDetalle)

        fun bind(
            detalle: Detalle_Producto,
            onEditar: (Detalle_Producto) -> Unit,
            onEliminar: (Detalle_Producto) -> Unit
        ) {

            txtId.text = detalle.id_detalle_producto.toString()
            txtTalla.text = "Talla: ${detalle.talla}"
            txtColor.text = "Color: ${detalle.color}"
            txtPrecio.text = "Precio: $${detalle.precio}"
            txtCategoria.text = "Categoría: ${detalle.id_categoria}"
            txtProducto.text = "Producto: ${detalle.id_producto}"


            if (!detalle.imagen.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(detalle.imagen)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(imgDetalle)
            } else {
                imgDetalle.setImageResource(R.drawable.ic_image_placeholder)
            }


            imgDetalle.setOnClickListener {
                val intent = Intent(itemView.context, ImagenFullscreenActivity::class.java)
                intent.putExtra("imagen", detalle.imagen)
                itemView.context.startActivity(intent)
            }


            btnEditar.setOnClickListener { onEditar(detalle) }
            btnEliminar.setOnClickListener { onEliminar(detalle) }
        }
    }
}
