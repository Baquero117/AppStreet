package com.example.appinterface.DataClass

data class Carrito(
    val id_carrito: Int,
    val id_cliente: Int,
    val id_detalle_producto: Int,
    val cantidad: Int,
    val precio_unitario: Double,
    val subtotal: Double
)
