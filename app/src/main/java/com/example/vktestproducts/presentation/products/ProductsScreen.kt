package com.example.vktestproducts.presentation.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.presentation.components.ErrorPlaceholder
import com.example.vktestproducts.presentation.components.LoadingPlaceholder
import kotlinx.coroutines.flow.flowOf

@Composable
fun ProductsScreen(
    productsViewModel: ProductsViewModel,
    navigateToDetailsScreen: (Int) -> Unit
) {
    val lazyPagingProducts = productsViewModel.products.collectAsLazyPagingItems()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (lazyPagingProducts.loadState.refresh) {
            is LoadState.Loading -> LoadingPlaceholder(modifier = Modifier.fillMaxSize())
            is LoadState.Error -> ErrorPlaceholder(
                modifier = Modifier.fillMaxSize(),
                onRefresh = {lazyPagingProducts.refresh()}
            )
            is LoadState.NotLoading -> ProductsList(
                lazyPagingProducts = lazyPagingProducts,
                navigateToDetailsScreen = navigateToDetailsScreen
            )
        }
    }
}

@Composable
private fun ProductsList(
    modifier: Modifier = Modifier,
    lazyPagingProducts: LazyPagingItems<Product>,
    navigateToDetailsScreen: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = lazyPagingProducts.itemCount,
            key = lazyPagingProducts.itemKey { it.id },
            contentType = lazyPagingProducts.itemContentType { "ProductItems" }
        ) { index ->
            val item = lazyPagingProducts[index]
            item?.let {
                ProductCard(product = item, navigateToDetailsScreen = navigateToDetailsScreen)
            }
        }
        item {
            when(lazyPagingProducts.loadState.append) {
                is LoadState.Loading -> LoadingPlaceholder()
                is LoadState.Error -> ErrorPlaceholder(onRefresh = {lazyPagingProducts.retry()})
                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    navigateToDetailsScreen: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .fillMaxWidth()
            .clickable {
                navigateToDetailsScreen(product.id)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.description,
                    maxLines = 2,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${product.price} $",
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview(device = "id:pixel_5", showBackground = true, showSystemUi = true)
@Composable
private fun ProductsListPreview() {
    val products = listOf(
        Product(
            id = 1,
            title = "Realme 8i",
            description = "Процессор Helio G96 Частота обновления экрана 120 Гц Аккумулятор 5000 мАч Дополнительные 5 ГБ виртуальной памяти Тройная Ai-камера 50 Мп...",
            price = 199,
            discountPercentage = 0.0,
            rating = 4.52,
            stock = 10,
            brand = "Realme",
            category = "smartphones",
            thumbnail = "",
            images = listOf("")
        ),
        Product(
            id = 2,
            title = "Realme 9i",
            description = "realme 9i заряжается на 36% быстрее, чем realme 8i. Смартфон оснащен пятиступенчатой защитой, обеспечивая более быструю и безопасную зарядку",
            price = 299,
            discountPercentage = 0.0,
            rating = 4.12,
            stock = 13,
            brand = "Realme",
            category = "smartphones",
            thumbnail = "",
            images = listOf("")
        )
    )
    val lazyPagingProducts = flowOf(PagingData.from(products)).collectAsLazyPagingItems()
    ProductsList(
        lazyPagingProducts = lazyPagingProducts,
        navigateToDetailsScreen = {}
    )
}

@Preview(device = "id:pixel_5")
@Composable
private fun ProductCardPreview() {
    ProductCard(
        product = Product(
            id = 1,
            title = "Realme 8i",
            description = "Процессор Helio G96 Частота обновления экрана 120 Гц Аккумулятор 5000 мАч Дополнительные 5 ГБ виртуальной памяти Тройная Ai-камера 50 Мп...",
            price = 199,
            discountPercentage = 0.0,
            rating = 4.52,
            stock = 10,
            brand = "Realme",
            category = "smartphones",
            thumbnail = "",
            images = listOf("")
        ),
        navigateToDetailsScreen = {}
    )
}