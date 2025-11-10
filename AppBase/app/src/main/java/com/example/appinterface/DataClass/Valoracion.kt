package com.example.appinterface.DataClass

data class Valoracion(
    val id_valoracion: Int,
    val id_cliente: Int,
    val id_producto: Int,
    val calificacion: Int,
    val comentario: String,
    val fecha_valoracion: String
)
