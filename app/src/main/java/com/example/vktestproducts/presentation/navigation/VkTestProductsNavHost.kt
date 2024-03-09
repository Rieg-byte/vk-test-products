package com.example.vktestproducts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vktestproducts.presentation.productdetails.ProductDetailsScreen
import com.example.vktestproducts.presentation.productdetails.ProductDetailsViewModel
import com.example.vktestproducts.presentation.products.ProductsScreen
import com.example.vktestproducts.presentation.products.ProductsViewModel

@Composable
fun VkTestProductsNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = VkTestProductsDestination.PRODUCTS_SCREEN.name
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = VkTestProductsDestination.PRODUCTS_SCREEN.name) {
            val productsViewModel = hiltViewModel<ProductsViewModel>()
            ProductsScreen(
                productsViewModel = productsViewModel,
                navigateToDetailsScreen = {id ->
                    navController.navigate(
                        route = "${VkTestProductsDestination.PRODUCTS_SCREEN}/$id",
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "${VkTestProductsDestination.PRODUCTS_SCREEN}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val productDetailsViewModel = hiltViewModel<ProductDetailsViewModel>()
            ProductDetailsScreen(
                productDetailsViewModel = productDetailsViewModel,
                navigateUp = {navController.navigateUp()}
            )
        }
    }
}