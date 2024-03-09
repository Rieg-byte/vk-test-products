package com.example.vktestproducts.data.repository.products

import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<PagingData<Product>>
    suspend fun getSingleProduct(id: Int): Product
}