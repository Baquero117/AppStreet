package com.example.appinterface

import com.example.appinterface.DataClass.Cliente
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
import com.example.appinterface.Adapter.ClienteAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteActivity : AppCompatActivity() {
    private lateinit var cliente: Cliente

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

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


    fun crearCliente(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correoElectronico = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val direccion = findViewById<EditText>(R.id.direccion)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        cliente = Cliente(
            0,
            nombre.text.toString(),
            apellido.text.toString(),
            contrasena.text.toString(),
            direccion.text.toString(),
            telefono.text.toString(),
            correoElectronico.text.toString()
        )

        if (nombre.text.isNotEmpty() && correoElectronico.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearCliente(cliente)
                .enqueue(object : Callback<Cliente> {
                    override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Cliente creado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear cliente", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Cliente>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    fun mostrarClientes(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getClientes().enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = ClienteAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ClienteActivity, "No hay clientes registrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ClienteActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                Toast.makeText(this@ClienteActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun actualizarCliente(v: View) {
        val id = findViewById<EditText>(R.id.id_cliente)
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correoElectronico = findViewById<EditText>(R.id.correo_electronico)
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
                correoElectronico.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarCliente(id.text.toString().toInt(), clienteActualizado)
                .enqueue(object : Callback<Cliente> {
                    override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Cliente>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
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
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
