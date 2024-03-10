package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.data.models.ResponseProducts
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(private val productsService: ProductsService): ProductsRemoteDataSource {
    override suspend fun getProducts(skip: Int, limit: Int): ResponseProducts = productsService.getProducts(skip, limit)
    override suspend fun getProductsByQuery(skip: Int, limit: Int, query: String): ResponseProducts = productsService.getProductsByQuery(skip, limit, query)
    override suspend fun getSingleProduct(id: Int): Product = productsService.getSingleProduct(id)
}