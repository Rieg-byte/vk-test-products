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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepository: ProductsRepository): ViewModel() {
    private val _productsState: MutableStateFlow<ProductsState> = MutableStateFlow(ProductsState())
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()

    init {
        getProducts()
    }

    fun showCategoriesBottomSheet() {
        _productsState.update {
            it.copy(showBottomSheet = true)
        }
        getCategories()
    }

    fun closeCategoriesBottomSheet() = _productsState.update {
        it.copy(
            showBottomSheet = false
        )
    }

    fun onSelectCategory(nameCategory: String) {
        _productsState.update {
            it.copy(
                categoryIsSelect = true,
                nameSelectedCategory = nameCategory
            )
        }
        getProductsByCategory(nameCategory)
    }

    fun onDeselectCategory() {
        _productsState.update {
            it.copy(
                categoryIsSelect = false,
                nameSelectedCategory = ""
            )
        }
        getProducts()
    }

    fun onRefreshCategories() {
        _productsState.update {
            it.copy(
                categoriesState = CategoriesState.Loading
            )
        }
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        try {
            val categories = productsRepository.getCategories()
            _productsState.update {
                it.copy(
                    categoriesState = CategoriesState.Success(categories = categories)
                )
            }
        } catch (e: HttpException) {
            _productsState.update {
                it.copy(
                    categoriesState = CategoriesState.Error
                )
            }
        } catch (e: IOException) {
            _productsState.update {
                it.copy(
                    categoriesState = CategoriesState.Error
                )
            }
        }
    }

    private fun getProducts() = _productsState.update {
        it.copy(
            products = productsRepository.getProducts().cachedIn(viewModelScope)
        )
    }

    private fun getProductsByCategory(nameCategory: String) = _productsState.update {
        it.copy(
            products = productsRepository.getProductsByCategory(nameCategory).cachedIn(viewModelScope)
        )
    }
}