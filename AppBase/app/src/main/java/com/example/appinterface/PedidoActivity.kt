package com.example.appinterface

import com.example.appinterface.DataClass.Pedido
import com.example.appinterface.Api.RetrofitInstance
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.PedidoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoActivity : AppCompatActivity() {
    private lateinit var pedido: Pedido

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

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

    fun crearPedido(v: View) {
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val fechaPedido = findViewById<EditText>(R.id.fecha_pedido)
        val total = findViewById<EditText>(R.id.total)
        val estado = findViewById<EditText>(R.id.estado)

        pedido = Pedido(
            0,
            idCliente.text.toString().toInt(),
            fechaPedido.text.toString(),
            total.text.toString().toDouble(),
            estado.text.toString()
        )

        if (idCliente.text.isNotEmpty() && fechaPedido.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearPedido(pedido)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Pedido creado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear pedido", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun mostrarPedidos(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyPedidos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getPedidos().enqueue(object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = PedidoAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@PedidoActivity, "No hay pedidos disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PedidoActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                Toast.makeText(this@PedidoActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarPedido(v: View) {
        val id = findViewById<EditText>(R.id.id_pedido)
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val fechaPedido = findViewById<EditText>(R.id.fecha_pedido)
        val total = findViewById<EditText>(R.id.total)
        val estado = findViewById<EditText>(R.id.estado)

        if (!id.text.isNullOrEmpty()) {
            val pedidoActualizado = Pedido(
                id.text.toString().toInt(),
                idCliente.text.toString().toInt(),
                fechaPedido.text.toString(),
                total.text.toString().toDouble(),
                estado.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarPedido(id.text.toString().toInt(), pedidoActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Pedido actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar pedido", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun eliminarPedido(v: View) {
        val id = findViewById<EditText>(R.id.id_pedido)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarPedido(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Pedido eliminado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar pedido", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
