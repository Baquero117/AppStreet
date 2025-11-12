package com.example.appinterface

import com.example.appinterface.DataClass.Categoria
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.CategoriaAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriaActivity : AppCompatActivity() {
    private lateinit var categoria: Categoria

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)

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


    fun crearCategoria(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)

        categoria = Categoria(0, nombre.text.toString())

        if (nombre.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearCategoria(categoria)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Categoría creada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(applicationContext, "Por favor, ingresa un nombre", Toast.LENGTH_SHORT).show()
        }
    }


    fun mostrarCategorias(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyCategorias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = CategoriaAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@CategoriaActivity, "No hay categorías disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CategoriaActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(this@CategoriaActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun actualizarCategoria(v: View) {
        val id = findViewById<EditText>(R.id.id_categoria)
        val nombre = findViewById<EditText>(R.id.nombre)

        if (!id.text.isNullOrEmpty()) {
            val categoriaActualizada = Categoria(
                id.text.toString().toInt(),
                nombre.text.toString()
            )

            val call = RetrofitInstance.api2kotlin.actualizarCategoria(
                id.text.toString().toInt(),
                categoriaActualizada
            )

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Categoría actualizada correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API", "Error del servidor: ${response.code()}")
                        Toast.makeText(applicationContext, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Error en la conexión: ${t.message}")
                    Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(applicationContext, "Por favor ingresa el ID de la categoría", Toast.LENGTH_SHORT).show()
        }
    }


    fun eliminarCategoria(v: View) {
        val id = findViewById<EditText>(R.id.id_categoria)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarCategoria(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Categoría eliminada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar categoría", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(applicationContext, "Por favor ingresa el ID de la categoría", Toast.LENGTH_SHORT).show()
        }
    }
}
