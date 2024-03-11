package com.example.vktestproducts.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.vktestproducts.ui.theme.VkTestProductsTheme

@Composable
fun NotFoundPlaceholder(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light theme", showBackground = true)
@Composable
private fun NotFoundPlaceholderPreview() {
    VkTestProductsTheme {
        Surface {
            NotFoundPlaceholder(
                modifier = Modifier.fillMaxSize(),
                text = "Ничего не найдено"
            )
        }
    }
}