package com.example.vktestproducts.data.repository.products

import com.example.vktestproducts.data.remote.ProductsPagingSource

interface ProductsRepository {
    fun productsPagingSource(): ProductsPagingSource
}