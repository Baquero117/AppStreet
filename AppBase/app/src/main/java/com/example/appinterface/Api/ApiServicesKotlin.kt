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



    @GET("categoria")
    fun getCategorias(): Call<List<Categoria>>

    @POST("categoria")
    fun crearCategoria(@Body categoria: Categoria): Call<Categoria>

    @PUT("categoria/{id}")
    fun actualizarCategoria(@Path("id") id: Int, @Body categoria: Categoria): Call<Categoria>

    @DELETE("categoria/{id}")
    fun eliminarCategoria(@Path("id") id: Int): Call<Void>




    @GET("detalle_producto")
    fun getDetalleProducto(): Call<List<Detalle_Producto>>

    @POST("detalle_producto")
    fun crearDetalleProducto(@Body detalleProducto: Detalle_Producto): Call<Detalle_Producto>

    @PUT("detalle_producto/{id}")
    fun actualizarDetalleProducto(@Path("id") id: Int, @Body detalleProducto: Detalle_Producto): Call<Detalle_Producto>

    @DELETE("detalle_producto/{id}")
    fun eliminarDetalleProducto(@Path("id") id: Int): Call<Void>


    @GET("producto")
    fun getProductos(): Call<List<Producto>>

    @POST("producto")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("producto/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Call<Producto>

    @DELETE("producto/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>





    @GET("pedido")
    fun getPedidos(): Call<List<Pedido>>

    @POST("pedido")
    fun crearPedido(@Body pedido: Pedido): Call<Pedido>

    @PUT("pedido/{id}")
    fun actualizarPedido(@Path("id") id: Int, @Body pedido: Pedido): Call<Pedido>

    @DELETE("pedido/{id}")
    fun eliminarPedido(@Path("id") id: Int): Call<Void>





    @GET("promocion")
    fun getPromociones(): Call<List<Promocion>>

    @POST("promocion")
    fun crearPromocion(@Body promocion: Promocion): Call<Promocion>

    @PUT("promocion{id}")
    fun actualizarPromocion(@Path("id") id: Int, @Body promocion: Promocion): Call<Promocion>

    @DELETE("promocion/{id}")
    fun eliminarPromocion(@Path("id") id: Int): Call<Void>

    @GET("valoracion")
    fun getValoraciones(): Call<List<Valoracion>>

    @POST("valoracion")
    fun crearValoracion(@Body valoracion: Valoracion): Call<Valoracion>

    @PUT("valoracion/{id}")
    fun actualizarValoracion(@Path("id") id: Int, @Body valoracion: Valoracion): Call<Valoracion>

    @DELETE("/valoracion/{id}")
    fun eliminarValoracion(@Path("id") id: Int): Call<Void>


    @GET("cliente")
    fun getClientes(): Call<List<Cliente>>

    @POST("cliente")
    fun crearCliente(@Body cliente: Cliente): Call<Cliente>

    @PUT("cliente/{id}")
    fun actualizarCliente(@Path("id") id: Int, @Body cliente: Cliente): Call<Cliente>

    @DELETE("cliente/{id}")
    fun eliminarCliente(@Path("id") id: Int): Call<Void>






    @GET("vendedor")
    fun getVendedores(): Call<List<Vendedor>>

    @POST("vendedor")
    fun crearVendedor(@Body vendedor: Vendedor): Call<Vendedor>

    @PUT("vendedor/{id}")
    fun actualizarVendedor(@Path("id") id: Int, @Body vendedor: Vendedor): Call<Vendedor>

    @DELETE("vendedor/{id}")
    fun eliminarVendedor(@Path("id") id: Int): Call<Void>





}