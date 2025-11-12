package com.example.appinterface

import com.example.appinterface.DataClass.Valoracion
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
import com.example.appinterface.Adapter.ValoracionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValoracionActivity : AppCompatActivity() {
    private lateinit var valoracion: Valoracion

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valoracion)

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

    fun crearValoracion(v: View) {
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val idProducto = findViewById<EditText>(R.id.id_producto)
        val calificacion = findViewById<EditText>(R.id.calificacion)
        val comentario = findViewById<EditText>(R.id.comentario)
        val fechaValoracion = findViewById<EditText>(R.id.fecha_valoracion)

        valoracion = Valoracion(
            0,
            idCliente.text.toString().toInt(),
            idProducto.text.toString().toInt(),
            calificacion.text.toString().toInt(),
            comentario.text.toString(),
            fechaValoracion.text.toString()
        )

        if (comentario.text.isNotEmpty() && calificacion.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearValoracion(valoracion)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Valoración creada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al crear valoración", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun mostrarValoraciones(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyValoraciones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getValoraciones().enqueue(object : Callback<List<Valoracion>> {
            override fun onResponse(call: Call<List<Valoracion>>, response: Response<List<Valoracion>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = ValoracionAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ValoracionActivity, "No hay valoraciones disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ValoracionActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Valoracion>>, t: Throwable) {
                Toast.makeText(this@ValoracionActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarValoracion(v: View) {
        val id = findViewById<EditText>(R.id.id_valoracion)
        val idCliente = findViewById<EditText>(R.id.id_cliente)
        val idProducto = findViewById<EditText>(R.id.id_producto)
        val calificacion = findViewById<EditText>(R.id.calificacion)
        val comentario = findViewById<EditText>(R.id.comentario)
        val fechaValoracion = findViewById<EditText>(R.id.fecha_valoracion)

        if (!id.text.isNullOrEmpty()) {
            val valoracionActualizada = Valoracion(
                id.text.toString().toInt(),
                idCliente.text.toString().toInt(),
                idProducto.text.toString().toInt(),
                calificacion.text.toString().toInt(),
                comentario.text.toString(),
                fechaValoracion.text.toString()
            )

            RetrofitInstance.api2kotlin.actualizarValoracion(id.text.toString().toInt(), valoracionActualizada)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Valoración actualizada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar valoración", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun eliminarValoracion(v: View) {
        val id = findViewById<EditText>(R.id.id_valoracion)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarValoracion(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Valoración eliminada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar valoración", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
