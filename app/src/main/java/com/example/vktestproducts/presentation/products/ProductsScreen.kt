package com.example.vktestproducts.presentation.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vktestproducts.R
import com.example.vktestproducts.presentation.common.components.ErrorPlaceholder
import com.example.vktestproducts.presentation.common.components.LazyPagingProducts
import com.example.vktestproducts.presentation.common.components.LoadingPlaceholder
import com.example.vktestproducts.presentation.common.components.NotFoundPlaceholder
import com.example.vktestproducts.presentation.common.components.ProductsListPlaceholder

@Composable
fun ProductsScreen(
    productsViewModel: ProductsViewModel = hiltViewModel(),
    navigateToSearchScreen: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit
) {
    val lazyPagingProducts = productsViewModel.products.collectAsLazyPagingItems()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProductsTopBar(
            title = stringResource(id = R.string.products),
            navigationIcon = Icons.Rounded.Search,
            onNavigationClick = navigateToSearchScreen,
            navigationContentDescription = stringResource(id = R.string.search)
        )
        LazyPagingProducts(
            modifier = Modifier.fillMaxSize(),
            lazyPagingProducts = lazyPagingProducts,
            loadingPlaceholder = { LoadingPlaceholder(modifier = Modifier.fillMaxSize()) },
            errorPlaceholder = {
                ErrorPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = {lazyPagingProducts.refresh()}
                )
            },
            notLoadingPlaceholder = {
                ProductsListPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    products = lazyPagingProducts,
                    navigateToDetailsScreen = navigateToDetailsScreen,
                    loadingPlaceholder = { LoadingPlaceholder(modifier = Modifier.fillMaxWidth()) },
                    errorPlaceholder = { ErrorPlaceholder(
                        onRefresh = {lazyPagingProducts.retry()},
                        modifier = Modifier.fillMaxWidth()
                    ) },
                    notFoundPlaceholder = {
                        NotFoundPlaceholder(text = stringResource(id = R.string.not_found))
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTopBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: ImageVector,
    onNavigationClick: () -> Unit = {},
    navigationContentDescription: String?
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = title)},
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(imageVector = navigationIcon, contentDescription = navigationContentDescription)
            }
        }
    )
}

