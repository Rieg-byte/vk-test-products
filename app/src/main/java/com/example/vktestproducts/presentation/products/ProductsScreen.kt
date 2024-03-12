package com.example.vktestproducts.presentation.products

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.vktestproducts.ui.theme.VkTestProductsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    productsViewModel: ProductsViewModel = hiltViewModel(),
    navigateToSearchScreen: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit
) {
    val productsState by productsViewModel.productsState.collectAsState()
    val lazyPagingProducts = productsState.products.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProductsTopBar(
            title = stringResource(id = R.string.products),
            navigationIcon = Icons.Rounded.Search,
            onNavigationClick = navigateToSearchScreen,
            navigationContentDescription = stringResource(id = R.string.search)
        )
        ProductsChipsRow(
            productsState = productsState,
            onClickCategory = productsViewModel::showCategoriesBottomSheet,
            onDeselectCategory = productsViewModel::onDeselectCategory
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
    CategoriesBottomSheet(
        sheetState = sheetState,
        scope = scope,
        showBottomSheet = productsState.showBottomSheet,
        onClickCategory = productsViewModel::onSelectCategory,
        onDismissRequest = productsViewModel::closeCategoriesBottomSheet,
        categoriesState = productsState.categoriesState,
        onRefreshCategories = productsViewModel::onRefreshCategories
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriesBottomSheet(
    sheetState: SheetState,
    scope: CoroutineScope,
    showBottomSheet: Boolean,
    onClickCategory: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onRefreshCategories: () -> Unit,
    categoriesState: CategoriesState
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            when (categoriesState) {
                is CategoriesState.Error -> ErrorPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = onRefreshCategories
                )
                is CategoriesState.Loading -> LoadingPlaceholder(modifier = Modifier.fillMaxSize())
                is CategoriesState.Success -> {
                    val categories = categoriesState.categories
                    CategoriesBottomSheetButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                            onClickCategory(categories[tabIndex])
                        },
                        label = stringResource(id = R.string.select))
                    LazyColumn {
                        itemsIndexed(categories) { index, _ ->
                            CategoryItem(
                                selected = tabIndex == index ,
                                onClick = { tabIndex = index },
                                nameCategory = categories[index]
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun CategoriesBottomSheetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onClick) {
            Text(label)
        }
    }
}

@Composable
private fun ProductsChipsRow(
    modifier: Modifier = Modifier,
    productsState: ProductsState,
    onClickCategory: () -> Unit,
    onDeselectCategory: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterProductsChip(
            selected = productsState.categoryIsSelect,
            onClick = onClickCategory,
            onDeselect = onDeselectCategory,
            label =
            if (!productsState.categoryIsSelect)
                stringResource(id = R.string.category_unselected)
            else stringResource(id = R.string.category_selected, productsState.nameSelectedCategory)
        )
    }
}
@Composable
private fun FilterProductsChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    onDeselect: () -> Unit,
    label: String,
) {
    FilterChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(label)
        },
        selected = selected,
        trailingIcon = if (selected) {
            {
                IconButton(
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                    onClick = onDeselect
                ) {
                    Icon(
                        imageVector = VkTestProductsIcons.Close,
                        contentDescription = null,
                    )
                }
            }
        } else {
            null
        },
    )
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    nameCategory: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = nameCategory)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductsTopBar(
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun CategoriesBottomSheetPreview() {
    VkTestProductsTheme {
        Surface {
            CategoriesBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                scope = rememberCoroutineScope(),
                showBottomSheet = true,
                onClickCategory = { /*TODO*/ },
                onDismissRequest = { /*TODO*/ },
                categoriesState = CategoriesState.Success(
                    listOf("Смартфоны", "Телевизоры", "Ноутбуки")),
                onRefreshCategories = { /*TODO*/ }
            )
        }
    }
}
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun FilterProductsChipPreview(){
    VkTestProductsTheme {
        Surface {
            FilterProductsChip(
                selected = true,
                onClick = { /*TODO*/ },
                label = "Категория: Смартфоны",
                onDeselect = { /*TODO*/ }
            )
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun ProductsTopBarPreview() {
    VkTestProductsTheme {
        Surface {
            ProductsTopBar(
                title = "Тест",
                navigationIcon = VkTestProductsIcons.Search,
                navigationContentDescription = null)
        }
    }
}
