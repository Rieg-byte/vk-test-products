package com.example.vktestproducts.presentation.products

import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductsState(
    val products: Flow<PagingData<Product>> = emptyFlow()
)
