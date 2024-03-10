package com.example.vktestproducts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vktestproducts.presentation.productdetails.ProductDetailsScreen
import com.example.vktestproducts.presentation.products.ProductsScreen
import com.example.vktestproducts.presentation.search.SearchScreen

@Composable
fun VkTestProductsNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = VkTestProductsDestination.PRODUCTS_SCREEN.name
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = VkTestProductsDestination.PRODUCTS_SCREEN.name) {
            ProductsScreen(
                navigateToSearchScreen = { navController.navigate(route = VkTestProductsDestination.SEARCH_SCREEN.name) },
                navigateToDetailsScreen = {id ->
                    navController.navigate(
                        route = "${VkTestProductsDestination.DETAIL_SCREEN}/$id",
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = VkTestProductsDestination.SEARCH_SCREEN.name) {
            SearchScreen(
                onNavigateBack = { navController.navigateUp() },
                navigateToDetailsScreen = { id ->
                    navController.navigate(
                        route = "${VkTestProductsDestination.DETAIL_SCREEN}/$id",
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "${VkTestProductsDestination.DETAIL_SCREEN}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            ProductDetailsScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}