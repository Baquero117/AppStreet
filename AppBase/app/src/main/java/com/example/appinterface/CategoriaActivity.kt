package com.example.appinterface

import com.example.appinterface.DataClass.Categoria
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.CategoriaAdapter

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

class CategoriaActivity : AppCompatActivity() {

    // Lista completa para búsquedas
    private var listaCategorias = listOf<Categoria>()

    private lateinit var adapter: CategoriaAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var txtId: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtBuscar: EditText
    private lateinit var btnBuscar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)

        txtId = findViewById(R.id.id_categoria)
        txtNombre = findViewById(R.id.nombre)
        txtBuscar = findViewById(R.id.txtBuscarId)
        btnBuscar = findViewById(R.id.btnBuscarId)

        recyclerView = findViewById(R.id.RecyCategorias)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = CategoriaAdapter(
            categorias = listOf(),
            onEditar = { categoria -> cargarDatosParaEditar(categoria) },
            onEliminar = { categoria -> eliminarDesdeLista(categoria) }
        )

        recyclerView.adapter = adapter


        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnBuscar.setOnClickListener {
            buscarCategoriaPorId()
        }
    }


    fun crearCategoria(v: View) {
        val nombre = txtNombre.text.toString()

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor escribe un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        val categoria = Categoria(0, nombre)

        RetrofitInstance.api2kotlin.crearCategoria(categoria)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Categoría creada", Toast.LENGTH_SHORT).show()
                        mostrarCategorias(v)
                    } else {
                        Toast.makeText(applicationContext, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun mostrarCategorias(v: View) {

        RetrofitInstance.api2kotlin.getCategorias()
            .enqueue(object : Callback<List<Categoria>> {
                override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: listOf()

                        // guardar copia para búsquedas
                        listaCategorias = lista

                        adapter.actualizarLista(lista)

                    } else {
                        Toast.makeText(this@CategoriaActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                    Toast.makeText(this@CategoriaActivity, "Error en conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun buscarCategoriaPorId() {

        if (txtBuscar.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtBuscar.text.toString().toInt()

        val categoriaEncontrada = listaCategorias.find { it.id_categoria == id }

        if (categoriaEncontrada != null) {
            adapter.actualizarLista(listOf(categoriaEncontrada))
        } else {
            Toast.makeText(this, "No se encontró categoría con ID $id", Toast.LENGTH_SHORT).show()
        }
    }


    private fun cargarDatosParaEditar(categoria: Categoria) {
        txtId.setText(categoria.id_categoria.toString())
        txtNombre.setText(categoria.nombre)

        Toast.makeText(this, "Editando categoría ${categoria.id_categoria}", Toast.LENGTH_SHORT).show()
    }


    fun actualizarCategoria(v: View) {
        val idTexto = txtId.text.toString()
        val nombreTexto = txtNombre.text.toString()

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID", Toast.LENGTH_SHORT).show()
            return
        }

        val categoria = Categoria(idTexto.toInt(), nombreTexto)

        RetrofitInstance.api2kotlin.actualizarCategoria(categoria.id_categoria, categoria)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Actualizada correctamente", Toast.LENGTH_SHORT).show()
                        mostrarCategorias(v)
                    } else {
                        Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun eliminarDesdeLista(categoria: Categoria) {

        RetrofitInstance.api2kotlin.eliminarCategoria(categoria.id_categoria)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CategoriaActivity, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                        mostrarCategorias(View(this@CategoriaActivity)) // refrescar lista
                    } else {
                        Toast.makeText(this@CategoriaActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@CategoriaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun eliminarCategoria(v: View) {
        val idTexto = txtId.text.toString()

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.api2kotlin.eliminarCategoria(idTexto.toInt())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                        mostrarCategorias(v)
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
