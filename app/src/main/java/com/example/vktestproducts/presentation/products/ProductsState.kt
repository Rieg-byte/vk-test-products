package com.example.vktestproducts.presentation.products

import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductsState(
    val products: Flow<PagingData<Product>> = emptyFlow(),
    val showBottomSheet: Boolean = false,
    val categoriesState: CategoriesState = CategoriesState.Loading,
    val categoryIsSelect: Boolean = false,
    val nameSelectedCategory: String = ""
)


sealed interface CategoriesState {
    data class Success(
        val categories: List<String>
    ): CategoriesState
    object Error: CategoriesState
    object Loading: CategoriesState
}
