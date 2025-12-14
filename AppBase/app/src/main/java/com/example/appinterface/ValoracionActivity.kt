package com.example.appinterface

import com.example.appinterface.DataClass.Valoracion
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.ValoracionAdapter

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

class ValoracionActivity : AppCompatActivity() {

    private var listaValoraciones = listOf<Valoracion>()
    private lateinit var valoracionAdapter: ValoracionAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valoracion)


        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }


        findViewById<Button>(R.id.btnBuscarId).setOnClickListener {
            buscarValoracionPorId()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.RecyValoraciones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        valoracionAdapter = ValoracionAdapter(
            listOf(),
            onEditar = { valoracion -> (valoracion) },
            onEliminar = { valoracion -> eliminarValoracionDirecto(valoracion) }
        )

        recyclerView.adapter = valoracionAdapter
    }



    fun mostrarValoraciones(v: View) {
        RetrofitInstance.api2kotlin(this).getValoraciones()
            .enqueue(object : Callback<List<Valoracion>> {
                override fun onResponse(
                    call: Call<List<Valoracion>>,
                    response: Response<List<Valoracion>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        if (!data.isNullOrEmpty()) {
                            listaValoraciones = data
                            valoracionAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@ValoracionActivity, "No hay valoraciones", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ValoracionActivity, "Error API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Valoracion>>, t: Throwable) {
                    Toast.makeText(this@ValoracionActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun buscarValoracionPorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()

        val valEncontrada = listaValoraciones.find { it.id_valoracion == id }

        if (valEncontrada != null) {
            valoracionAdapter.actualizarLista(listOf(valEncontrada))
        } else {
            Toast.makeText(this, "No se encontró la valoración con ID $id", Toast.LENGTH_SHORT).show()
        }
    }



    private fun eliminarValoracionDirecto(valoracion: Valoracion) {
        RetrofitInstance.api2kotlin(this).eliminarValoracion(valoracion.id_valoracion)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Valoración eliminada", Toast.LENGTH_SHORT).show()
                        mostrarValoraciones(View(this@ValoracionActivity))
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
