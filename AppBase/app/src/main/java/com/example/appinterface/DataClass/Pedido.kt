package com.example.appinterface.DataClass

data class Pedido(
    val id_pedido: Int,
    val id_cliente: Int,
    val fecha_pedido: String,
    val total: Double,
    val estado: String
)
