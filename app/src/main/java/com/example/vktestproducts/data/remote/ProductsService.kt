package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.data.models.ResponseProducts
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsService {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ResponseProducts

    @GET("products/search")
    suspend fun getProductsByQuery(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("q") query: String
    ): ResponseProducts

    @GET("products/{id}")
    suspend fun getSingleProduct(
        @Path("id") id: Int
    ): Product

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("/products/category/{nameCategory}")
    suspend fun getProductsByCategory(
        @Path("nameCategory") nameCategory: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): ResponseProducts
}