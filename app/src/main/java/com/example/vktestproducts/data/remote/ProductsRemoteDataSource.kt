package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.ResponseProducts

interface ProductsRemoteDataSource {
    suspend fun getProducts(skip: Int, limit: Int): ResponseProducts
}