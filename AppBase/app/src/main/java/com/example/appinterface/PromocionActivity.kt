package com.example.appinterface

import com.example.appinterface.DataClass.Promocion
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.PromocionAdapter
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

class PromocionActivity : AppCompatActivity() {

    private var listaPromociones = listOf<Promocion>()
    private lateinit var promocionAdapter: PromocionAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promocion)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        btnBuscar.setOnClickListener {
            buscarPromocionPorId()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.RecyPromociones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        promocionAdapter = PromocionAdapter(
            listOf(),
            onEditar = { promocion -> llenarCampos(promocion) },
            onEliminar = { promocion -> eliminarPromocionDirecto(promocion) }
        )

        recyclerView.adapter = promocionAdapter
    }


    fun crearPromocion(v: View) {
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val descuento = findViewById<EditText>(R.id.descuento)
        val fechaInicio = findViewById<EditText>(R.id.fecha_inicio)
        val fechaFin = findViewById<EditText>(R.id.fecha_fin)
        val idProducto = findViewById<EditText>(R.id.id_producto)

        val promocion = Promocion(
            0,
            descripcion.text.toString(),
            descuento.text.toString().toDouble(),
            fechaInicio.text.toString(),
            fechaFin.text.toString(),
            idProducto.text.toString().toInt()
        )

        RetrofitInstance.api2kotlin.crearPromocion(promocion)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Promoción creada", Toast.LENGTH_SHORT).show()
                        mostrarPromociones(View(this@PromocionActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error servidor ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun mostrarPromociones(v: View) {
        RetrofitInstance.api2kotlin.getPromociones()
            .enqueue(object : Callback<List<Promocion>> {
                override fun onResponse(call: Call<List<Promocion>>, response: Response<List<Promocion>>) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        if (!data.isNullOrEmpty()) {
                            listaPromociones = data
                            promocionAdapter.actualizarLista(data)
                        } else {
                            Toast.makeText(this@PromocionActivity, "No hay promociones", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@PromocionActivity, "Error API", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Promocion>>, t: Throwable) {
                    Toast.makeText(this@PromocionActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun buscarPromocionPorId() {
        val txtId = findViewById<EditText>(R.id.txtBuscarId)

        if (txtId.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val id = txtId.text.toString().toInt()

        val promo = listaPromociones.find { it.id_promocion == id }

        if (promo != null) {
            promocionAdapter.actualizarLista(listOf(promo))
        } else {
            Toast.makeText(this, "No se encontró la promoción con ID $id", Toast.LENGTH_SHORT).show()
        }
    }


    private fun llenarCampos(promocion: Promocion) {
        findViewById<EditText>(R.id.id_promocion).setText(promocion.id_promocion.toString())
        findViewById<EditText>(R.id.descripcion).setText(promocion.descripcion)
        findViewById<EditText>(R.id.descuento).setText(promocion.descuento.toString())
        findViewById<EditText>(R.id.fecha_inicio).setText(promocion.fecha_inicio)
        findViewById<EditText>(R.id.fecha_fin).setText(promocion.fecha_fin)
        findViewById<EditText>(R.id.id_producto).setText(promocion.id_producto.toString())
    }


    private fun eliminarPromocionDirecto(promocion: Promocion) {
        RetrofitInstance.api2kotlin.eliminarPromocion(promocion.id_promocion)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Promoción eliminada", Toast.LENGTH_SHORT).show()
                        mostrarPromociones(View(this@PromocionActivity))
                    } else {
                        Toast.makeText(applicationContext, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun actualizarPromocion(v: View) {
        val id = findViewById<EditText>(R.id.id_promocion)
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val descuento = findViewById<EditText>(R.id.descuento)
        val fechaInicio = findViewById<EditText>(R.id.fecha_inicio)
        val fechaFin = findViewById<EditText>(R.id.fecha_fin)
        val idProducto = findViewById<EditText>(R.id.id_producto)

        if (!id.text.isNullOrEmpty()) {
            val promoActualizada = Promocion(
                id.text.toString().toInt(),
                descripcion.text.toString(),
                descuento.text.toString().toDouble(),
                fechaInicio.text.toString(),
                fechaFin.text.toString(),
                idProducto.text.toString().toInt()
            )

            RetrofitInstance.api2kotlin.actualizarPromocion(promoActualizada.id_promocion, promoActualizada)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Promoción actualizada", Toast.LENGTH_SHORT).show()
                            mostrarPromociones(View(this@PromocionActivity))
                        } else {
                            Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            Toast.makeText(this, "Ingresa el ID de la promoción", Toast.LENGTH_SHORT).show()
        }
    }
}
