package com.example.appinterface.Api

import com.example.appinterface.DataClass.Categoria
import com.example.appinterface.DataClass.Cliente
import com.example.appinterface.DataClass.Detalle_Producto
import com.example.appinterface.DataClass.Pedido
import com.example.appinterface.DataClass.Producto
import com.example.appinterface.DataClass.Promocion
import com.example.appinterface.DataClass.Valoracion
import com.example.appinterface.DataClass.Vendedor
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServicesKotlin {
    @GET("/usuarios")
    fun getPersonas(): Call<List<String>>


    @GET("categoria")
    fun getCategorias(): Call<List<Categoria>>

    @POST("categoria")
    fun crearCategoria(@Body categoria: Categoria): Call<Categoria>

    @PUT("categoria/{id}")
    fun actualizarCategoria(@Path("id") id: Int, @Body categoria: Categoria): Call<Categoria>

    @DELETE("categoria/{id}")
    fun eliminarCategoria(@Path("id") id: Int): Call<Void>


    @GET("/clientes")
    fun getClientes(): Call<List<Cliente>>

    @POST("/clientes")
    fun crearCliente(@Body cliente: Cliente): Call<Cliente>

    @PUT("/clientes/{id}")
    fun actualizarCliente(@Path("id") id: Int, @Body cliente: Cliente): Call<Cliente>

    @DELETE("/clientes/{id}")
    fun eliminarCliente(@Path("id") id: Int): Call<Void>





    @GET("/detalle_productos")
    fun getDetalleProducto(): Call<List<Detalle_Producto>>

    @POST("/detalle_productos")
    fun crearDetalleProducto(@Body detalleProducto: Detalle_Producto): Call<Detalle_Producto>

    @PUT("/detalle_productos/{id}")
    fun actualizarDetalleProducto(@Path("id") id: Int, @Body detalleProducto: Detalle_Producto): Call<Detalle_Producto>

    @DELETE("/detalle_productos/{id}")
    fun eliminarDetalleProducto(@Path("id") id: Int): Call<Void>


    @GET("/producto")
    fun getProductos(): Call<List<Producto>>

    @POST("/producto")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("/producto/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Call<Producto>

    @DELETE("/producto/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>










    @GET("/pedidos")
    fun getPedidos(): Call<List<Pedido>>

    @POST("/pedidos")
    fun crearPedido(@Body pedido: Pedido): Call<Pedido>

    @PUT("/pedidos/{id}")
    fun actualizarPedido(@Path("id") id: Int, @Body pedido: Pedido): Call<Pedido>

    @DELETE("/pedidos/{id}")
    fun eliminarPedido(@Path("id") id: Int): Call<Void>





    @GET("/promociones")
    fun getPromociones(): Call<List<Promocion>>

    @POST("/promociones")
    fun crearPromocion(@Body promocion: Promocion): Call<Promocion>

    @PUT("/promociones/{id}")
    fun actualizarPromocion(@Path("id") id: Int, @Body promocion: Promocion): Call<Promocion>

    @DELETE("/promociones/{id}")
    fun eliminarPromocion(@Path("id") id: Int): Call<Void>

    @GET("/valoraciones")
    fun getValoraciones(): Call<List<Valoracion>>

    @POST("/valoraciones")
    fun crearValoracion(@Body valoracion: Valoracion): Call<Valoracion>

    @PUT("/valoraciones/{id}")
    fun actualizarValoracion(@Path("id") id: Int, @Body valoracion: Valoracion): Call<Valoracion>

    @DELETE("/valoraciones/{id}")
    fun eliminarValoracion(@Path("id") id: Int): Call<Void>










    @GET("/vendedor")
    fun getVendedores(): Call<List<Vendedor>>

    // -------------------- CREAR VENDEDOR --------------------
    @POST("/vendedores")
    fun crearVendedor(@Body vendedor: Vendedor): Call<Vendedor>

    // -------------------- ACTUALIZAR VENDEDOR --------------------
    @PUT("/vendedores/{id}")
    fun actualizarVendedor(@Path("id") id: Int, @Body vendedor: Vendedor): Call<Vendedor>

    // -------------------- ELIMINAR VENDEDOR --------------------
    @DELETE("/vendedores/{id}")
    fun eliminarVendedor(@Path("id") id: Int): Call<Void>





}