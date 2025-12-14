package com.example.appinterface.DataClass

data class LoginResponse(
    val token: String,
    val tipo: String,
    val usuario: Usuario
)
