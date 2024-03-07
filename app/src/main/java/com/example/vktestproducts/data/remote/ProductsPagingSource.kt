package com.example.vktestproducts.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vktestproducts.data.models.Product

class ProductsPagingSource (private val productsRemoteDataSource: ProductsRemoteDataSource): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val skip = params.key ?: 0
            val limit = params.loadSize
            Log.d("PagingCheck", "skip = $skip, limit = $limit")
            val response = productsRemoteDataSource.getProducts(skip, limit)
            val products = response.products
            val nextKey = if (products.size < limit) null else skip + limit
            val prevKey = if (skip == 0) null else skip - limit
            LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}