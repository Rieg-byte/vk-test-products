package com.example.vktestproducts.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.vktestproducts.data.models.Product

@Composable
fun LazyPagingProducts(
    modifier: Modifier = Modifier,
    lazyPagingProducts: LazyPagingItems<Product>,
    loadingPlaceholder: @Composable () -> Unit,
    errorPlaceholder: @Composable () -> Unit,
    notLoadingPlaceholder: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        when (lazyPagingProducts.loadState.refresh) {
            is LoadState.Loading -> loadingPlaceholder()
            is LoadState.Error -> errorPlaceholder()
            is LoadState.NotLoading -> notLoadingPlaceholder()
        }
    }
}
