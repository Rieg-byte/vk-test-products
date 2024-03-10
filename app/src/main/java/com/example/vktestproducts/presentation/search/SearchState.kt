package com.example.vktestproducts.presentation.search

import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class SearchState(
    val searchValue: String = "",
    val searchResultState: SearchResultState = SearchResultState.Default
)

sealed interface SearchResultState {
    data class Result(
        val products: Flow<PagingData<Product>> = emptyFlow()
    ): SearchResultState
    object Default: SearchResultState
}