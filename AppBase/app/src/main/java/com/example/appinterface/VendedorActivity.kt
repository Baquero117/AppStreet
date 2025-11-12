package com.example.appinterface

import com.example.appinterface.DataClass.Vendedor
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
import com.example.appinterface.Adapter.VendedorAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendedorActivity : AppCompatActivity() {
    private lateinit var vendedor: Vendedor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendedor)

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

    fun crearVendedor(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correoElectronico = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        vendedor = Vendedor(
            0,
            nombre.text.toString(),
            apellido.text.toString(),
            correoElectronico.text.toString(),
            telefono.text.toString(),
            contrasena.text.toString()
        )

        if (nombre.text.isNotEmpty() && correoElectronico.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearVendedor(vendedor)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor creado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear vendedor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun mostrarVendedores(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyVendedores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getVendedores().enqueue(object : Callback<List<Vendedor>> {
            override fun onResponse(call: Call<List<Vendedor>>, response: Response<List<Vendedor>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = VendedorAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@VendedorActivity, "No hay vendedores registrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@VendedorActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Vendedor>>, t: Throwable) {
                Toast.makeText(this@VendedorActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarVendedor(v: View) {
        val id = findViewById<EditText>(R.id.id_vendedor)
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val correoElectronico = findViewById<EditText>(R.id.correo_electronico)
        val telefono = findViewById<EditText>(R.id.telefono)
        val contrasena = findViewById<EditText>(R.id.contrasena)

        if (!id.text.isNullOrEmpty()) {
            val vendedorActualizado = Vendedor(
                id.text.toString().toInt(),
                nombre.text.toString(),
                apellido.text.toString(),
                correoElectronico.text.toString(),
                telefono.text.toString(),
                contrasena.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarVendedor(id.text.toString().toInt(), vendedorActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar vendedor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun eliminarVendedor(v: View) {
        val id = findViewById<EditText>(R.id.id_vendedor)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarVendedor(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Vendedor eliminado correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar vendedor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
