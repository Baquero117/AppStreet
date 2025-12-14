package com.example.appinterface

import com.example.appinterface.DataClass.Vendedor
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.VendedorAdapter
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

class VendedorActivity : AppCompatActivity() {

    private var listaVendedores = listOf<Vendedor>()    // Lista interna para búsqueda
    private lateinit var vendedorAdapter: VendedorAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendedor)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        btnBuscar.setOnClickListener {
            buscarVendedorPorId()
        }

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.RecyVendedores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        vendedorAdapter = VendedorAdapter(
            listOf(),
            onEditar = { vendedor -> llenarCampos(vendedor) },
            onEliminar = { vendedor -> eliminarVendedorDirecto(vendedor) }
        )

        recyclerView.adapter = vendedorAdapter
    }


    fun crearVendedor(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correo = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        val vendedor = Vendedor(
            0,
            nombre.text.toString(),
            apellido.text.toString(),
            correo.text.toString(),
            telefono.text.toString(),
            contrasena.text.toString()
        )

        if (nombre.text.isNotEmpty() && correo.text.isNotEmpty()) {

            RetrofitInstance.api2kotlin(this).crearVendedor(vendedor)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor creado correctamente", Toast.LENGTH_SHORT).show()
                            mostrarVendedores(View(this@VendedorActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error del servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(applicationContext, "Ingresa nombre y correo", Toast.LENGTH_SHORT).show()
        }
    }


    fun mostrarVendedores(v: View) {

        RetrofitInstance.api2kotlin(this).getVendedores()
            .enqueue(object : Callback<List<Vendedor>> {
                override fun onResponse(call: Call<List<Vendedor>>, response: Response<List<Vendedor>>) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        if (!data.isNullOrEmpty()) {
                            listaVendedores = data
                            vendedorAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@VendedorActivity, "No hay vendedores registrados", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@VendedorActivity, "Error en la API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Vendedor>>, t: Throwable) {
                    Toast.makeText(this@VendedorActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun buscarVendedorPorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()
        val vendedorEncontrado = listaVendedores.find { it.id_vendedor == id }

        if (vendedorEncontrado != null) {
            vendedorAdapter.actualizarLista(listOf(vendedorEncontrado))
        } else {
            Toast.makeText(this, "No se encontró vendedor con ID $id", Toast.LENGTH_SHORT).show()
        }
    }


    private fun llenarCampos(vendedor: Vendedor) {
        findViewById<EditText>(R.id.id_vendedor).setText(vendedor.id_vendedor.toString())
        findViewById<EditText>(R.id.nombre).setText(vendedor.nombre)
        findViewById<EditText>(R.id.apellido).setText(vendedor.apellido)
        findViewById<EditText>(R.id.correo_electronico).setText(vendedor.correo_electronico)
        findViewById<EditText>(R.id.telefono).setText(vendedor.telefono)
        findViewById<EditText>(R.id.contrasena).setText(vendedor.contrasena)
    }


    private fun eliminarVendedorDirecto(vendedor: Vendedor) {
        RetrofitInstance.api2kotlin(this).eliminarVendedor(vendedor.id_vendedor)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Vendedor eliminado", Toast.LENGTH_SHORT).show()
                        mostrarVendedores(View(this@VendedorActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun actualizarVendedor(v: View) {
        val id = findViewById<EditText>(R.id.id_vendedor)
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correo = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        if (!id.text.isNullOrEmpty()) {

            val vendedorActualizado = Vendedor(
                id.text.toString().toInt(),
                nombre.text.toString(),
                apellido.text.toString(),
                correo.text.toString(),
                telefono.text.toString(),
                contrasena.text.toString()
            )

            RetrofitInstance.api2kotlin(this).actualizarVendedor(id.text.toString().toInt(), vendedorActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor actualizado", Toast.LENGTH_SHORT).show()
                            mostrarVendedores(View(this@VendedorActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar vendedor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(this, "Ingresa un ID para actualizar", Toast.LENGTH_SHORT).show()
        }
    }


    fun eliminarVendedor(v: View) {
        val id = findViewById<EditText>(R.id.id_vendedor)

        if (!id.text.isNullOrEmpty()) {

            RetrofitInstance.api2kotlin(this).eliminarVendedor(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor eliminado correctamente", Toast.LENGTH_SHORT).show()
                            mostrarVendedores(View(this@VendedorActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar vendedor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show()
        }
    }
}
