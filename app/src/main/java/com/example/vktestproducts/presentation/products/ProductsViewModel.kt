package com.example.vktestproducts.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.vktestproducts.data.repository.products.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepository: ProductsRepository): ViewModel() {
    private val _productsState: MutableStateFlow<ProductsState> = MutableStateFlow(ProductsState())
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() = _productsState.update {
        it.copy(
            products = productsRepository.getProducts().cachedIn(viewModelScope)
        )
    }
}