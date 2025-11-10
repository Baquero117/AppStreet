package com.example.appinterface



import com.example.appinterface.DataClass.Detalle_Producto
import com.example.appinterface.Api.RetrofitInstance
import android.annotation.SuppressLint

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.DetalleProductoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class DetalleProdcutoActivity : AppCompatActivity() {
    private lateinit var detalleProducto: Detalle_Producto

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // -------------------- CREAR DETALLE PRODUCTO --------------------
    fun crearDetalleProducto(v: View) {
        val talla = findViewById<EditText>(R.id.talla)
        val color = findViewById<EditText>(R.id.color)
        val imagen = findViewById<EditText>(R.id.imagen)
        val id_producto = findViewById<EditText>(R.id.id_producto)
        val id_categoria = findViewById<EditText>(R.id.id_categoria)
        val precio = findViewById<EditText>(R.id.precio)

        detalleProducto = Detalle_Producto(
            0,
            talla.text.toString(),
            color.text.toString(),
            imagen.text.toString(),
            id_producto.text.toString().toInt(),
            id_categoria.text.toString().toInt(),
            precio.text.toString().toDouble()
        )

        if (talla.text.isNotEmpty() && color.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearDetalleProducto(detalleProducto)
                .enqueue(object : Callback<Detalle_Producto> {
                    override fun onResponse(call: Call<Detalle_Producto>, response: Response<Detalle_Producto>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Detalle creado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear detalle", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Detalle_Producto>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // -------------------- MOSTRAR DETALLES PRODUCTO --------------------
    fun mostrarDetallesProducto(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyDetalles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getDetalleProducto().enqueue(object : Callback<List<Detalle_Producto>> {
            override fun onResponse(call: Call<List<Detalle_Producto>>, response: Response<List<Detalle_Producto>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = DetalleProductoAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@DetalleProdcutoActivity, "No hay detalles disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetalleProdcutoActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Detalle_Producto>>, t: Throwable) {
                Toast.makeText(this@DetalleProdcutoActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // -------------------- ACTUALIZAR DETALLE PRODUCTO --------------------
    fun actualizarDetalleProducto(v: View) {
        val id = findViewById<EditText>(R.id.id_detalle_producto)
        val talla = findViewById<EditText>(R.id.talla)
        val color = findViewById<EditText>(R.id.color)
        val imagen = findViewById<EditText>(R.id.imagen)
        val id_producto = findViewById<EditText>(R.id.id_producto)
        val id_categoria = findViewById<EditText>(R.id.id_categoria)
        val precio = findViewById<EditText>(R.id.precio)

        if (!id.text.isNullOrEmpty()) {
            val detalleActualizado = Detalle_Producto(
                id.text.toString().toInt(),
                talla.text.toString(),
                color.text.toString(),
                imagen.text.toString(),
                id_producto.text.toString().toInt(),
                id_categoria.text.toString().toInt(),
                precio.text.toString().toDouble()
            )

            RetrofitInstance.api2kotlin.actualizarDetalleProducto(id.text.toString().toInt(), detalleActualizado)
                .enqueue(object : Callback<Detalle_Producto> {
                    override fun onResponse(call: Call<Detalle_Producto>, response: Response<Detalle_Producto>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Detalle actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar detalle", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Detalle_Producto>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // -------------------- ELIMINAR DETALLE PRODUCTO --------------------
    fun eliminarDetalleProducto(v: View) {
        val id = findViewById<EditText>(R.id.id_detalle_producto)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarDetalleProducto(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Detalle eliminado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar detalle", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
