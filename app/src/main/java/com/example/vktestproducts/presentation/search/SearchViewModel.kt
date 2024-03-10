package com.example.vktestproducts.presentation.search

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
class SearchViewModel @Inject constructor(private val productsRepository: ProductsRepository): ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
    fun onSearchValueChange(value: String) = _searchState.update {
        it.copy(
            searchValue = value,
            searchResultState = SearchResultState.Default
        )
    }

    fun onSearchTrigger(value: String) = _searchState.update {
        if (value.isNotEmpty()) {
            it.copy(
                searchResultState = SearchResultState.Result(
                    products = productsRepository.getProductsByQuery(value).cachedIn(viewModelScope)
                )
            )
        } else {
            it.copy(
                searchResultState = SearchResultState.Default,
            )
        }
    }
}