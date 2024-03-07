package com.example.vktestproducts.data.repository.products

import com.example.vktestproducts.data.remote.ProductsPagingSource
import com.example.vktestproducts.data.remote.ProductsRemoteDataSource
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsRemoteDataSource: ProductsRemoteDataSource): ProductsRepository {
    override fun productsPagingSource(): ProductsPagingSource = ProductsPagingSource(productsRemoteDataSource)
}