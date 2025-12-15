package com.example.appinterface

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.DataClass.LoginRequest
import com.example.appinterface.DataClass.LoginResponse
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


        val request = LoginRequest(
            correo_electronico = correo,
            contrasena = contrasena
        )

        RetrofitInstance.api2kotlin(this).login(request)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val loginResponse = response.body()!!


                        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                        prefs.edit()
                            .putString("jwt", loginResponse.token)
                            .putString("tipo", loginResponse.tipo)
                            .putString("nombre", loginResponse.usuario.nombre)
                            .apply()


                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido ${loginResponse.usuario.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()


                        if (loginResponse.tipo == "administrador") {
                            startActivity(
                                Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
                            )
                        }

                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
