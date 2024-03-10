package com.example.vktestproducts.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vktestproducts.R
import com.example.vktestproducts.presentation.common.components.ErrorPlaceholder
import com.example.vktestproducts.presentation.common.components.LazyPagingProducts
import com.example.vktestproducts.presentation.common.components.LoadingPlaceholder
import com.example.vktestproducts.presentation.common.components.NotFoundPlaceholder
import com.example.vktestproducts.presentation.common.components.ProductsListPlaceholder
import com.example.vktestproducts.presentation.common.icons.VkTestProductsIcons

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit
) {
    val searchState by searchViewModel.searchState.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        SearchTopBar(
            onNavigateBack = onNavigateBack,
            searchValue = searchState.searchValue,
            onSearchValueChange = searchViewModel::onSearchValueChange,
            onSearchTrigger = searchViewModel::onSearchTrigger
        )
        SearchBody(
            searchResultState = searchState.searchResultState,
            navigateToDetailsScreen = navigateToDetailsScreen
        )

    }
}

@Composable
private fun SearchBody(
    searchResultState: SearchResultState,
    navigateToDetailsScreen: (Int) -> Unit
) {
    when (searchResultState) {
        is SearchResultState.Default -> SearchDefault()
        is SearchResultState.Result -> {
            val lazyPagingProducts = searchResultState.products.collectAsLazyPagingItems()
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
                        products = lazyPagingProducts,
                        navigateToDetailsScreen = navigateToDetailsScreen,
                        loadingPlaceholder = { LoadingPlaceholder(
                            modifier = Modifier.fillMaxWidth()
                        ) },
                        errorPlaceholder = {
                            ErrorPlaceholder(
                                onRefresh = {lazyPagingProducts.retry()},
                                modifier = Modifier.fillMaxWidth()
                            ) },
                        notFoundPlaceholder = {
                            NotFoundPlaceholder(
                                text = stringResource(id = R.string.not_found),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun SearchDefault() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.enter_text))
    }
}

@Composable
private fun SearchTopBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    searchValue: String,
    onSearchValueChange: (String) -> Unit = {},
    onSearchTrigger: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = VkTestProductsIcons.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        SearchField(
            searchValue = searchValue,
            onSearchValueChange = onSearchValueChange,
            onSearchTrigger = onSearchTrigger
        )
    }
}

@Composable
private fun SearchField(
    searchValue: String,
    onSearchValueChange: (String) -> Unit = {},
    onSearchTrigger: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchValue,
        onValueChange = onSearchValueChange,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        leadingIcon = { Icon(imageVector = VkTestProductsIcons.Search, contentDescription = stringResource(id = R.string.search)) },
        trailingIcon = {
            if (searchValue.isNotEmpty()){
                IconButton(onClick = { onSearchValueChange("") }){
                    Icon(
                        imageVector = VkTestProductsIcons.Close,
                        contentDescription = stringResource(id = R.string.clear_input),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        shape = RoundedCornerShape(32.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearchTrigger(searchValue)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

