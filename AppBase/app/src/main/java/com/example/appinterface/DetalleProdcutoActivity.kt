package com.example.appinterface

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinterface.Adapter.DetalleProductoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.DataClass.Detalle_Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleProductoActivity : AppCompatActivity() {

    private var listaDetalles = listOf<Detalle_Producto>()
    private lateinit var detalleAdapter: DetalleProductoAdapter


    private var imagenSeleccionadaUri: String? = null


    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                imagenSeleccionadaUri = it.toString()
                findViewById<ImageView>(R.id.imgPreviewDetalle).setImageURI(it)
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)


        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }


        findViewById<Button>(R.id.btnSeleccionarImagen).setOnClickListener {
            seleccionarImagen.launch(arrayOf("image/*"))
        }


        findViewById<Button>(R.id.btnBuscarId).setOnClickListener {
            buscarDetallePorId()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.RecyDetalles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        detalleAdapter = DetalleProductoAdapter(
            listOf(),
            onEditar = { detalle -> llenarCampos(detalle) },
            onEliminar = { detalle -> eliminarDetalleDirecto(detalle) }
        )

        recyclerView.adapter = detalleAdapter
    }


    fun crearDetalleProducto(v: View) {
        val detalle = obtenerDetalleDesdeFormulario(0)

        if (detalle.talla.isNotEmpty() && detalle.color.isNotEmpty()) {
            RetrofitInstance.api2kotlin(this).crearDetalleProducto(detalle)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Detalle creado", Toast.LENGTH_SHORT).show()
                            limpiarFormulario()
                            mostrarDetalles(View(this@DetalleProductoActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al crear detalle", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    fun mostrarDetalles(v: View) {
        RetrofitInstance.api2kotlin(this).getDetalleProducto()
            .enqueue(object : Callback<List<Detalle_Producto>> {
                override fun onResponse(
                    call: Call<List<Detalle_Producto>>,
                    response: Response<List<Detalle_Producto>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (!data.isNullOrEmpty()) {
                            listaDetalles = data
                            detalleAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@DetalleProductoActivity, "No hay detalles", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@DetalleProductoActivity, "Error en la API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Detalle_Producto>>, t: Throwable) {
                    Toast.makeText(this@DetalleProductoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun buscarDetallePorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()
        val detalleEncontrado = listaDetalles.find { it.id_detalle_producto == id }

        if (detalleEncontrado != null) {
            detalleAdapter.actualizarLista(listOf(detalleEncontrado))
        } else {
            Toast.makeText(this, "Detalle no encontrado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun llenarCampos(detalle: Detalle_Producto) {
        findViewById<EditText>(R.id.id_detalle_producto).setText(detalle.id_detalle_producto.toString())
        findViewById<EditText>(R.id.talla).setText(detalle.talla)
        findViewById<EditText>(R.id.color).setText(detalle.color)
        findViewById<EditText>(R.id.id_producto).setText(detalle.id_producto.toString())
        findViewById<EditText>(R.id.id_categoria).setText(detalle.id_categoria.toString())
        findViewById<EditText>(R.id.precio).setText(detalle.precio.toString())


        imagenSeleccionadaUri = detalle.imagen
        Glide.with(this)
            .load(detalle.imagen)
            .into(findViewById(R.id.imgPreviewDetalle))
    }


    fun actualizarDetalleProducto(v: View) {
        val idTxt = findViewById<EditText>(R.id.id_detalle_producto)

        if (idTxt.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show()
            return
        }

        val detalleActualizado =
            obtenerDetalleDesdeFormulario(idTxt.text.toString().toInt())

        RetrofitInstance.api2kotlin(this)
            .actualizarDetalleProducto(detalleActualizado.id_detalle_producto, detalleActualizado)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Detalle actualizado", Toast.LENGTH_SHORT).show()
                        limpiarFormulario()
                        mostrarDetalles(View(this@DetalleProductoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun eliminarDetalleDirecto(detalle: Detalle_Producto) {
        RetrofitInstance.api2kotlin(this).eliminarDetalleProducto(detalle.id_detalle_producto)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Detalle eliminado", Toast.LENGTH_SHORT).show()
                        mostrarDetalles(View(this@DetalleProductoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun obtenerDetalleDesdeFormulario(id: Int): Detalle_Producto {
        return Detalle_Producto(
            id,
            findViewById<EditText>(R.id.talla).text.toString(),
            findViewById<EditText>(R.id.color).text.toString(),
            imagenSeleccionadaUri ?: "",
            findViewById<EditText>(R.id.id_producto).text.toString().toInt(),
            findViewById<EditText>(R.id.id_categoria).text.toString().toInt(),
            findViewById<EditText>(R.id.precio).text.toString().toDouble()
        )
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.id_detalle_producto).setText("")
        findViewById<EditText>(R.id.talla).setText("")
        findViewById<EditText>(R.id.color).setText("")
        findViewById<EditText>(R.id.id_producto).setText("")
        findViewById<EditText>(R.id.id_categoria).setText("")
        findViewById<EditText>(R.id.precio).setText("")
        findViewById<ImageView>(R.id.imgPreviewDetalle).setImageResource(0)

        imagenSeleccionadaUri = null
    }
}
