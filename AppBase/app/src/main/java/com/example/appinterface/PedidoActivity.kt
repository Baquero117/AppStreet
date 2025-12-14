package com.example.appinterface

import com.example.appinterface.DataClass.Pedido
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.PedidoAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoActivity : AppCompatActivity() {

    private var listaPedidos = listOf<Pedido>()
    private lateinit var pedidoAdapter: PedidoAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        btnBuscar.setOnClickListener {
            buscarPedidoPorId()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.RecyPedidos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pedidoAdapter = PedidoAdapter(
            listOf(),
            onEditar = { pedido -> llenarCampos(pedido) },
            onEliminar = { pedido -> eliminarPedidoDirecto(pedido) }
        )

        recyclerView.adapter = pedidoAdapter
    }


    fun crearPedido(v: View) {
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val fechaPedido = findViewById<EditText>(R.id.fecha_pedido)
        val total = findViewById<EditText>(R.id.total)
        val estado = findViewById<EditText>(R.id.estado)

        if (idCliente.text.isEmpty() || fechaPedido.text.isEmpty()) {
            Toast.makeText(this, "Ingresa ID Cliente y Fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val pedido = Pedido(
            0,
            idCliente.text.toString().toInt(),
            fechaPedido.text.toString(),
            total.text.toString().toDouble(),
            estado.text.toString()
        )

        RetrofitInstance.api2kotlin(this).crearPedido(pedido)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Pedido creado", Toast.LENGTH_SHORT).show()
                        mostrarPedidos(View(this@PedidoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al crear", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun mostrarPedidos(v: View) {
        RetrofitInstance.api2kotlin(this).getPedidos()
            .enqueue(object : Callback<List<Pedido>> {
                override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        if (!data.isNullOrEmpty()) {
                            listaPedidos = data
                            pedidoAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@PedidoActivity, "No hay pedidos", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@PedidoActivity, "Error en API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                    Toast.makeText(this@PedidoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun buscarPedidoPorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()

        val pedidoEncontrado = listaPedidos.find { it.id_pedido == id }

        if (pedidoEncontrado != null) {
            pedidoAdapter.actualizarLista(listOf(pedidoEncontrado))
        } else {
            Toast.makeText(this, "No se encontró el pedido con ID $id", Toast.LENGTH_SHORT).show()
        }
    }


    private fun llenarCampos(pedido: Pedido) {
        findViewById<EditText>(R.id.id_pedido).setText(pedido.id_pedido.toString())
        findViewById<EditText>(R.id.id_cliente).setText(pedido.id_cliente.toString())
        findViewById<EditText>(R.id.fecha_pedido).setText(pedido.fecha_pedido)
        findViewById<EditText>(R.id.total).setText(pedido.total.toString())
        findViewById<EditText>(R.id.estado).setText(pedido.estado)
    }


    private fun eliminarPedidoDirecto(pedido: Pedido) {
        RetrofitInstance.api2kotlin(this).eliminarPedido(pedido.id_pedido)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Pedido eliminado", Toast.LENGTH_SHORT).show()
                        mostrarPedidos(View(this@PedidoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun actualizarPedido(v: View) {
        val id = findViewById<EditText>(R.id.id_pedido)
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val fechaPedido = findViewById<EditText>(R.id.fecha_pedido)
        val total = findViewById<EditText>(R.id.total)
        val estado = findViewById<EditText>(R.id.estado)

        if (id.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa el ID del pedido", Toast.LENGTH_SHORT).show()
            return
        }

        val pedidoActualizado = Pedido(
            id.text.toString().toInt(),
            idCliente.text.toString().toInt(),
            fechaPedido.text.toString(),
            total.text.toString().toDouble(),
            estado.text.toString()
        )

        RetrofitInstance.api2kotlin(this).actualizarPedido(id.text.toString().toInt(), pedidoActualizado)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Pedido actualizado", Toast.LENGTH_SHORT).show()
                        mostrarPedidos(View(this@PedidoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun eliminarPedido(v: View) {
        val id = findViewById<EditText>(R.id.id_pedido)

        if (id.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa el ID del pedido", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.api2kotlin(this).eliminarPedido(id.text.toString().toInt())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Pedido eliminado", Toast.LENGTH_SHORT).show()
                        mostrarPedidos(View(this@PedidoActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
