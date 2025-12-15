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
import com.example.appinterface.Adapter.ProductoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.DataClass.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActivity : AppCompatActivity() {

    private var listaProductos = listOf<Producto>()
    private lateinit var productoAdapter: ProductoAdapter


    private var imagenSeleccionadaUri: String? = null


    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                imagenSeleccionadaUri = it.toString()
                findViewById<ImageView>(R.id.imgPreview).setImageURI(it)
            }
        }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)


        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }


        findViewById<Button>(R.id.btnSeleccionarImagen).setOnClickListener {
            seleccionarImagen.launch(arrayOf("image/*"))
        }



        findViewById<Button>(R.id.btnBuscarId).setOnClickListener {
            buscarProductoPorId()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.RecyProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productoAdapter = ProductoAdapter(
            listOf(),
            onEditar = { producto -> llenarCampos(producto) },
            onEliminar = { producto -> eliminarProductoDirecto(producto) }
        )

        recyclerView.adapter = productoAdapter
    }


    fun crearProducto(v: View) {
        val producto = obtenerProductoDesdeFormulario(0)

        if (producto.nombre.isNotEmpty() && producto.descripcion.isNotEmpty()) {
            RetrofitInstance.api2kotlin(this).crearProducto(producto)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Producto creado", Toast.LENGTH_SHORT).show()
                            limpiarFormulario()
                            mostrarProductos(View(this@ProductoActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al crear producto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    fun mostrarProductos(v: View) {
        RetrofitInstance.api2kotlin(this).getProductos()
            .enqueue(object : Callback<List<Producto>> {
                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (!data.isNullOrEmpty()) {
                            listaProductos = data
                            productoAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@ProductoActivity, "No hay productos", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProductoActivity, "Error en la API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(this@ProductoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun buscarProductoPorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()
        val productoEncontrado = listaProductos.find { it.id_producto == id }

        if (productoEncontrado != null) {
            productoAdapter.actualizarLista(listOf(productoEncontrado))
        } else {
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun llenarCampos(producto: Producto) {
        findViewById<EditText>(R.id.id_producto).setText(producto.id_producto.toString())
        findViewById<EditText>(R.id.nombre).setText(producto.nombre)
        findViewById<EditText>(R.id.descripcion).setText(producto.descripcion)
        findViewById<EditText>(R.id.cantidad).setText(producto.cantidad.toString())
        findViewById<EditText>(R.id.id_vendedor).setText(producto.id_vendedor.toString())
        findViewById<EditText>(R.id.estado).setText(producto.estado)

        // 📸 Imagen
        imagenSeleccionadaUri = producto.imagen
        Glide.with(this)
            .load(producto.imagen)
            .into(findViewById(R.id.imgPreview))
    }


    fun actualizarProducto(v: View) {
        val idTxt = findViewById<EditText>(R.id.id_producto)

        if (idTxt.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show()
            return
        }

        val productoActualizado =
            obtenerProductoDesdeFormulario(idTxt.text.toString().toInt())

        RetrofitInstance.api2kotlin(this)
            .actualizarProducto(productoActualizado.id_producto, productoActualizado)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        limpiarFormulario()
                        mostrarProductos(View(this@ProductoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun eliminarProductoDirecto(producto: Producto) {
        RetrofitInstance.api2kotlin(this).eliminarProducto(producto.id_producto)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Producto eliminado", Toast.LENGTH_SHORT).show()
                        mostrarProductos(View(this@ProductoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun obtenerProductoDesdeFormulario(id: Int): Producto {
        return Producto(
            id,
            findViewById<EditText>(R.id.nombre).text.toString(),
            findViewById<EditText>(R.id.descripcion).text.toString(),
            findViewById<EditText>(R.id.cantidad).text.toString().toInt(),
            imagenSeleccionadaUri ?: "",
            findViewById<EditText>(R.id.id_vendedor).text.toString().toInt(),
            findViewById<EditText>(R.id.estado).text.toString()
        )
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.id_producto).setText("")
        findViewById<EditText>(R.id.nombre).setText("")
        findViewById<EditText>(R.id.descripcion).setText("")
        findViewById<EditText>(R.id.cantidad).setText("")
        findViewById<EditText>(R.id.id_vendedor).setText("")
        findViewById<EditText>(R.id.estado).setText("")
        findViewById<ImageView>(R.id.imgPreview)


        imagenSeleccionadaUri = null
    }
}
