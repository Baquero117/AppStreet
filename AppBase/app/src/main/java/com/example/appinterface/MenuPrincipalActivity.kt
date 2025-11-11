package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.databinding.ActivityMenuBinding

open class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnProductos.setOnClickListener {
            startActivity(Intent(this, ProductoActivity::class.java))
        }

        binding.btnClientes.setOnClickListener {
            startActivity(Intent(this, ClienteActivity::class.java))
        }



        binding.btnCategorias.setOnClickListener {
            startActivity(Intent(this, CategoriaActivity::class.java))
        }

        binding.btnDetallesProducto.setOnClickListener {
            startActivity(Intent(this, DetalleProdcutoActivity::class.java))
        }

        binding.btnValoraciones.setOnClickListener {
            startActivity(Intent(this, ValoracionActivity::class.java))
        }

        binding.btnPedidos.setOnClickListener {
            startActivity(Intent(this, PedidoActivity::class.java))
        }

        binding.btnPromociones.setOnClickListener {
            startActivity(Intent(this, PromocionActivity::class.java))
        }

        binding.btnVendedores.setOnClickListener {
            startActivity(Intent(this, VendedorActivity::class.java))
        }
    }
}


