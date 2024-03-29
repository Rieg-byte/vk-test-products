package com.example.vktestproducts.data.repository.products

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.data.remote.ProductsByCategoryPagingSource
import com.example.vktestproducts.data.remote.ProductsPagingSource
import com.example.vktestproducts.data.remote.ProductsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsRemoteDataSource: ProductsRemoteDataSource): ProductsRepository {
    override fun getProducts(): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 1
        ),
        pagingSourceFactory = { ProductsPagingSource(productsRemoteDataSource) }
    ).flow

    override fun getProductsByQuery(query: String): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 1
        ),
        pagingSourceFactory = { ProductsPagingSource(productsRemoteDataSource, query) }
    ).flow

    override suspend fun getSingleProduct(id: Int): Product = productsRemoteDataSource.getSingleProduct(id)

    override suspend fun getCategories(): List<String> = productsRemoteDataSource.getCategories()
    override fun getProductsByCategory(nameCategory: String): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 1
        ),
        pagingSourceFactory = { ProductsByCategoryPagingSource(productsRemoteDataSource, nameCategory) }
    ).flow
}