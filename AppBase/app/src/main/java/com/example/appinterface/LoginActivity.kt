package com.example.appinterface

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.DataClass.Vendedor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_vendedor)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            loginVendedor()
        }
    }

    private fun loginVendedor() {
        val correo = findViewById<EditText>(R.id.txtCorreoVendedor).text.toString()
        val contrasena = findViewById<EditText>(R.id.txtContrasenaVendedor).text.toString()

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.api2kotlin.getVendedores()
            .enqueue(object : Callback<List<Vendedor>> {
                override fun onResponse(call: Call<List<Vendedor>>, response: Response<List<Vendedor>>) {
                    if (response.isSuccessful) {

                        val lista = response.body() ?: emptyList()

                        val vendedor = lista.find { it.correo_electronico == correo }

                        if (vendedor == null) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Correo incorrecto o no registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        if (vendedor.contrasena == contrasena) {

                            Toast.makeText(
                                this@LoginActivity,
                                "Bienvenido ${vendedor.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(
                                this@LoginActivity,
                                MenuPrincipalActivity::class.java
                            )

                            intent.putExtra("id_vendedor", vendedor.id_vendedor)
                            intent.putExtra("nombre_vendedor", vendedor.nombre)

                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Contraseña incorrecta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Vendedor>>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
