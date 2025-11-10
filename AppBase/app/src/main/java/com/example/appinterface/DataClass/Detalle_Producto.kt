package com.example.appinterface.DataClass

data class Detalle_Producto(
    val id_detalle_producto: Int,
    val talla: String,
    val color: String,
    val imagen: String,
    val id_producto: Int,
    val id_categoria: Int,
    val precio: Double
)
