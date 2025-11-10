package com.example.appinterface

import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.DataClass.Categoria

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
import com.example.appinterface.Adapter.CategoriaAdapter
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

        val buttonGoToSecondActivity: Button = findViewById(R.id.buttonSegundaActividad)
        buttonGoToSecondActivity.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }
    }

    // -------------------- CREAR CATEGORÍA --------------------
    fun crearCategoria(v: View) {
        val nombre = findViewById<EditText>(R.id.nombre)

        categoria = Categoria(0, nombre.text.toString())

        if (!nombre.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.crearCategoria(categoria)
                .enqueue(object : Callback<Categoria> {
                    override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Categoría creada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear categoría", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Categoria>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // -------------------- MOSTRAR CATEGORÍAS --------------------
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

    // -------------------- ACTUALIZAR CATEGORÍA --------------------
    fun actualizarCategoria(v: View) {
        val id = findViewById<EditText>(R.id.id_categoria)
        val nombre = findViewById<EditText>(R.id.nombre)

        if (!id.text.isNullOrEmpty() && !nombre.text.isNullOrEmpty()) {
            val categoriaActualizada = Categoria(id.text.toString().toInt(), nombre.text.toString())

            RetrofitInstance.api2kotlin.actualizarCategoria(id.text.toString().toInt(), categoriaActualizada)
                .enqueue(object : Callback<Categoria> {
                    override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Categoría actualizada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Categoria>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // -------------------- ELIMINAR CATEGORÍA --------------------
    fun eliminarCategoria(v: View) {
        val id = findViewById<EditText>(R.id.id_categoria)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarCategoria(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Categoría eliminada correctamente", Toast.LENGTH_SHORT).show()
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
}
