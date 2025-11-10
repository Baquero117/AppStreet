package com.example.appinterface.DataClass

data class Promocion(
    val id_promocion: Int,
    val descripcion: String,
    val descuento: Double,
    val fecha_inicio: String,
    val fecha_fin: String,
    val id_producto: Int
)
