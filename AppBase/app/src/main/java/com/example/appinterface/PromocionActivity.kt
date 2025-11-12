package com.example.appinterface
import com.example.appinterface.DataClass.Promocion
import com.example.appinterface.Api.RetrofitInstance
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
import com.example.appinterface.Adapter.PromocionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromocionActivity : AppCompatActivity() {
    private lateinit var promocion: Promocion

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promocion)

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


    fun crearPromocion(v: View) {
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val descuento = findViewById<EditText>(R.id.descuento)
        val fechaInicio = findViewById<EditText>(R.id.fecha_inicio)
        val fechaFin = findViewById<EditText>(R.id.fecha_fin)
        val idProducto = findViewById<EditText>(R.id.id_producto)

        promocion = Promocion(
            0,
            descripcion.text.toString(),
            descuento.text.toString().toDouble(),
            fechaInicio.text.toString(),
            fechaFin.text.toString(),
            idProducto.text.toString().toInt()
        )

        if (descripcion.text.isNotEmpty() && descuento.text.isNotEmpty()) {
            RetrofitInstance.api2kotlin.crearPromocion(promocion)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Promoción creada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

        }
    }


    fun mostrarPromociones(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyPromociones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getPromociones().enqueue(object : Callback<List<Promocion>> {
            override fun onResponse(call: Call<List<Promocion>>, response: Response<List<Promocion>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val adapter = PromocionAdapter(data)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@PromocionActivity, "No hay promociones disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PromocionActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Promocion>>, t: Throwable) {
                Toast.makeText(this@PromocionActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
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
            val promocionActualizada = Promocion(
                id.text.toString().toInt(),
                descripcion.text.toString(),
                descuento.text.toString().toDouble(),
                fechaInicio.text.toString(),
                fechaFin.text.toString(),
                idProducto.text.toString().toInt()
            )


            val call = RetrofitInstance.api2kotlin.actualizarPromocion(
                id.text.toString().toInt(),
                promocionActualizada
            )
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Promoción actualizada con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API", "Error del servidor: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Error en la conexión: ${t.message}")
                }
            })

        }
    }


    fun eliminarPromocion(v: View) {
        val id = findViewById<EditText>(R.id.id_promocion)

        if (!id.text.isNullOrEmpty()) {
            RetrofitInstance.api2kotlin.eliminarPromocion(id.text.toString().toInt())
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Promoción eliminada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error al eliminar promoción", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}