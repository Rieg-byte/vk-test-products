package com.example.vktestproducts.presentation.productdetails

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vktestproducts.R
import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.presentation.common.components.ErrorPlaceholder
import com.example.vktestproducts.presentation.common.components.LoadingPlaceholder
import com.example.vktestproducts.presentation.common.icons.VkTestProductsIcons
import com.example.vktestproducts.ui.theme.VkTestProductsTheme
import com.example.vktestproducts.ui.theme.colorRating
import com.example.vktestproducts.ui.theme.discountBackground
import com.example.vktestproducts.ui.theme.onDiscountBackground

@Composable
fun ProductDetailsScreen(
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val productDetailsState by productDetailsViewModel.productDetailsState.collectAsState()
    Scaffold(
        topBar = { DetailAppBar(navigateUp = navigateUp) }
    ) { innerPadding ->
        ProductDetailsBody(
            modifier = Modifier
                .padding(innerPadding),
            productDetailsState = productDetailsState,
            productDetailsViewModel = productDetailsViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailAppBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    title: String = ""
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = VkTestProductsIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        }
    )
}

@Composable
private fun ProductDetailsBody(
    modifier: Modifier = Modifier,
    productDetailsState: ProductDetailsState,
    productDetailsViewModel: ProductDetailsViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        when(productDetailsState) {
            is ProductDetailsState.Success -> ProductDetail(product = productDetailsState.product)
            is ProductDetailsState.Error -> ErrorPlaceholder(
                modifier = Modifier.fillMaxSize(),
                onRefresh = productDetailsViewModel::retry
            )
            is ProductDetailsState.Loading -> LoadingPlaceholder(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ProductDetail(
    modifier: Modifier = Modifier,
    product: Product
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ImageHorizontalPager(images = product.images)
        TitleBrandCard(title = product.title, brand = product.brand)
        PriceRatingCard(
            price = product.price,
            discountPercentage = product.discountPercentage,
            rating = product.rating
        )
        ProductDescriptionCard(description = product.description)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageHorizontalPager(
    modifier: Modifier = Modifier,
    images: List<String>
) {
    val pagerState = rememberPagerState (pageCount = { images.size })
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[it])
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun TitleBrandCard(
    modifier: Modifier = Modifier,
    title: String,
    brand: String
    ) {
    Card(modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(start = 6.dp, end = 6.dp, top = 2.dp, bottom = 2.dp)
                ,
                text = brand,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 8.sp
            )
        }
    }
}

@Composable
private fun PriceRatingCard(
    modifier: Modifier = Modifier,
    price: Int,
    discountPercentage: Double,
    rating: Double
) {
    Card(modifier = modifier) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.price, price),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (discountPercentage > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.discountBackground)
                            .padding(4.dp),
                        text = stringResource(id = R.string.discount_percentage, discountPercentage.toString()),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onDiscountBackground,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp) ,
                    imageVector = Icons.Rounded.Star,
                    tint = colorRating,
                    contentDescription = null
                )
                Text(
                    text = rating.toString(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun ProductDescriptionCard(
    modifier: Modifier = Modifier,
    description: String
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.about_the_product),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun DetailAppBarPreview() {
    VkTestProductsTheme {
        Surface {
            DetailAppBar(navigateUp = { /*TODO*/ })
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun ImageHorizontalPagerPreview() {
    VkTestProductsTheme {
        Surface {
            ImageHorizontalPager(images = listOf("", ""))
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun TitleBrandCardPreview() {
    VkTestProductsTheme {
        Surface {
            TitleBrandCard(title = "iPhone 9", brand = "Apple")
        }
    }
}


@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun PriceCardPreview() {
    VkTestProductsTheme {
        Surface {
            PriceRatingCard(price = 2, discountPercentage = 0.0, rating = 2.44)
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun ProductDescriptionPreview() {
    VkTestProductsTheme {
        Surface {
            ProductDescriptionCard(description = "Какое-то описание")
        }
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun ProductDetailPreview() {
    VkTestProductsTheme {
        Surface {
            ProductDetail(
                product = Product(
                    id = 1,
                    title = "Realme 8i",
                    description = "Процессор Helio G96 Частота обновления экрана 120 Гц Аккумулятор 5000 мАч Дополнительные 5 ГБ виртуальной памяти Тройная Ai-камера 50 Мп...",
                    price = 199,
                    discountPercentage = 0.2,
                    rating = 4.52,
                    stock = 10,
                    brand = "Realme",
                    category = "smartphones",
                    thumbnail = "",
                    images = listOf("")
                )
            )
        }
    }
}
