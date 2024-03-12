package com.example.vktestproducts.data.repository.products

import androidx.paging.PagingData
import com.example.vktestproducts.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    /**
     * Возвращает товары
     *
     * @return Flow<PagingData<Product>>
     */
    fun getProducts(): Flow<PagingData<Product>>

    /**
     * Возвращает товары по запросу
     *
     * @param query запрос
     *
     * @return Flow<PagingData<Product>>
     */
    fun getProductsByQuery(query: String): Flow<PagingData<Product>>

    /**
     * Возвращает отдельный товар по его id
     *
     * @param id id товара
     */
    suspend fun getSingleProduct(id: Int): Product

    /**
     * Возвращает список категорий
     *
     * @return List<String>
     */
    suspend fun getCategories(): List<String>

    /**
     * Возвращает товары определённой категории
     *
     * @param nameCategory название категории
     *
     * @return Flow<PagingData<Product>>
     */
    fun getProductsByCategory(nameCategory: String): Flow<PagingData<Product>>
}