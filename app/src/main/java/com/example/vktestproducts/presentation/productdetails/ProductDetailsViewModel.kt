package com.example.vktestproducts.presentation.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktestproducts.data.repository.products.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository
): ViewModel() {
    private val _productDetailState: MutableStateFlow<ProductDetailsState> = MutableStateFlow(ProductDetailsState.Loading)
    val productDetailsState: StateFlow<ProductDetailsState> = _productDetailState.asStateFlow()
    private val id: Int = checkNotNull(savedStateHandle["id"])
    init {
        getProductDetail(id)
    }

    fun retry() = viewModelScope.launch {
        _productDetailState.value = ProductDetailsState.Loading
        getProductDetail(id)
    }

    private fun getProductDetail(id: Int) = viewModelScope.launch {
        try {
            val product = productsRepository.getSingleProduct(id)
            _productDetailState.value = ProductDetailsState.Success(
                product = product
            )
        } catch (e: HttpException) {
            _productDetailState.value = ProductDetailsState.Error
        } catch (e: IOException) {
            _productDetailState.value = ProductDetailsState.Error
        }
    }
}