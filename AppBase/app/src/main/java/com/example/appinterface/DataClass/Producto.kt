package com.example.appinterface.DataClass

data class Producto(
    val id_producto: Int,
    val nombre: String,
    val descripcion: String,
    val cantidad: Int,
    val imagen: String,
    val id_vendedor: Int,
    val estado: String
)
