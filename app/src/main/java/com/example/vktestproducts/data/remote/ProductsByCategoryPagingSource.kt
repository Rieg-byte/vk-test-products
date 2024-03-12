package com.example.vktestproducts.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vktestproducts.data.models.Product

class ProductsByCategoryPagingSource (
    private val productsRemoteDataSource: ProductsRemoteDataSource,
    private val category: String
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val skip = state.closestPageToPosition(anchorPosition) ?: return null
        return skip.prevKey ?: skip.nextKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val skip = params.key ?: 0
            val limit = params.loadSize
            val response = productsRemoteDataSource.getProductsByCategory(category, skip, limit)
            val products = response.products
            val nextKey = if (products.size < limit) null else skip + products.size
            val prevKey = if (skip == 0) null else skip - products.size
            Log.d("PagingCheck", "skip = $skip, limit = $limit, size = ${products.size}, prevKey = $prevKey, nextKey = $nextKey")
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