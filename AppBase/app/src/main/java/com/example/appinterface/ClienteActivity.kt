package com.example.appinterface

import com.example.appinterface.DataClass.Cliente
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.ClienteAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteActivity : AppCompatActivity() {


    private var listaClientes = listOf<Cliente>()


    private lateinit var clienteAdapter: ClienteAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }


        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        btnBuscar.setOnClickListener {
            buscarClientePorId()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.RecyClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        clienteAdapter = ClienteAdapter(
            listOf(),
            onEditar = { cliente -> llenarCampos(cliente) },
            onEliminar = { cliente -> eliminarClienteDirecto(cliente) }
        )
        recyclerView.adapter = clienteAdapter
    }



    fun crearCliente(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correo = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val direccion = findViewById<EditText>(R.id.direccion)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        val cliente = Cliente(
            0,
            nombre.text.toString(),
            apellido.text.toString(),
            contrasena.text.toString(),
            direccion.text.toString(),
            telefono.text.toString(),
            correo.text.toString()
        )

        if (nombre.text.isNotEmpty() && correo.text.isNotEmpty()) {

            RetrofitInstance.api2kotlin.crearCliente(cliente)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Cliente creado correctamente", Toast.LENGTH_SHORT).show()
                            mostrarClientes(View(this@ClienteActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(applicationContext, "Por favor ingresa nombre y correo", Toast.LENGTH_SHORT).show()
        }
    }



    fun mostrarClientes(v: View) {

        RetrofitInstance.api2kotlin.getClientes()
            .enqueue(object : Callback<List<Cliente>> {
                override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {

                    if (response.isSuccessful) {
                        val data = response.body()

                        if (!data.isNullOrEmpty()) {

                            listaClientes = data
                            clienteAdapter.actualizarLista(data)

                        } else {
                            Toast.makeText(this@ClienteActivity, "No hay clientes registrados", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@ClienteActivity, "Error en la API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                    Toast.makeText(this@ClienteActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }



    fun buscarClientePorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()

        val clienteEncontrado = listaClientes.find { it.id_cliente == id }

        if (clienteEncontrado != null) {
            clienteAdapter.actualizarLista(listOf(clienteEncontrado))
        } else {
            Toast.makeText(this, "No se encontró cliente con ID $id", Toast.LENGTH_SHORT).show()
        }
    }



    private fun llenarCampos(cliente: Cliente) {
        findViewById<EditText>(R.id.id_cliente).setText(cliente.id_cliente.toString())
        findViewById<EditText>(R.id.nombre).setText(cliente.nombre)
        findViewById<EditText>(R.id.apellido).setText(cliente.apellido)
        findViewById<EditText>(R.id.correo_electronico).setText(cliente.correo_electronico)
        findViewById<EditText>(R.id.telefono).setText(cliente.telefono)
        findViewById<EditText>(R.id.direccion).setText(cliente.direccion)
        findViewById<EditText>(R.id.contrasena).setText(cliente.contrasena)
    }



    private fun eliminarClienteDirecto(cliente: Cliente) {
        RetrofitInstance.api2kotlin.eliminarCliente(cliente.id_cliente)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                        mostrarClientes(View(this@ClienteActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }



    fun actualizarCliente(v: View) {
        val id = findViewById<EditText>(R.id.id_cliente)
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correo = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val direccion = findViewById<EditText>(R.id.direccion)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        if (!id.text.isNullOrEmpty()) {

            val clienteActualizado = Cliente(
                id.text.toString().toInt(),
                nombre.text.toString(),
                apellido.text.toString(),
                contrasena.text.toString(),
                direccion.text.toString(),
                telefono.text.toString(),
                correo.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarCliente(id.text.toString().toInt(), clienteActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                            mostrarClientes(View(this@ClienteActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error del servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(applicationContext, "Por favor ingresa el ID del cliente", Toast.LENGTH_SHORT).show()
        }
    }


    fun eliminarCliente(v: View) {
        val id = findViewById<EditText>(R.id.id_cliente)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarCliente(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show()
                            mostrarClientes(View(this@ClienteActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(applicationContext, "Por favor ingresa el ID del cliente", Toast.LENGTH_SHORT).show()
        }
    }
}
