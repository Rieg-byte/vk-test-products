package com.example.vktestproducts.presentation.productdetails

import com.example.vktestproducts.data.models.Product

sealed interface ProductDetailsState {
    data class Success(
        val product: Product
    ): ProductDetailsState
    object Error: ProductDetailsState
    object Loading: ProductDetailsState
}
