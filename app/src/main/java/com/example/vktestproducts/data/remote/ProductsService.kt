package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.ResponseProducts
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsService {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ResponseProducts
}