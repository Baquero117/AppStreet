package com.example.appinterface

import com.example.appinterface.DataClass.Producto
import com.example.appinterface.Api.RetrofitInstance
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ProductoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActivity : MenuPrincipalActivity() {
    private lateinit var producto: Producto

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun crearProducto(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val cantidad = findViewById<EditText>(R.id.cantidad)
        val imagen = findViewById<EditText>(R.id.imagen)
        val idVendedor = findViewById<EditText>(R.id.id_vendedor)
        val estado = findViewById<EditText>(R.id.estado)

        producto = Producto(
            0,
            nombre.text.toString(),
            descripcion.text.toString(),
            cantidad.text.toString().toInt(),
            imagen.text.toString(),
            idVendedor.text.toString().toInt(),
            estado.text.toString()
        )

        if (nombre.text.isNotEmpty() && descripcion.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearProducto(producto)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Producto creado correctamente", Toast.LENGTH_SHORT).show()
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
        val recyclerView = findViewById<RecyclerView>(R.id.RecyProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = ProductoAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ProductoActivity, "No hay productos disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProductoActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@ProductoActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarProducto(v: View) {
        val id = findViewById<EditText>(R.id.id_producto)
        val nombre = findViewById<EditText>(R.id.nombre)
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val cantidad = findViewById<EditText>(R.id.cantidad)
        val imagen = findViewById<EditText>(R.id.imagen)
        val idVendedor = findViewById<EditText>(R.id.id_vendedor)
        val estado = findViewById<EditText>(R.id.estado)

        if (!id.text.isNullOrEmpty()) {
            val productoActualizado = Producto(
                id.text.toString().toInt(),
                nombre.text.toString(),
                descripcion.text.toString(),
                cantidad.text.toString().toInt(),
                imagen.text.toString(),
                idVendedor.text.toString().toInt(),
                estado.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarProducto(id.text.toString().toInt(), productoActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun eliminarProducto(v: View) {
        val id = findViewById<EditText>(R.id.id_producto)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarProducto(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
