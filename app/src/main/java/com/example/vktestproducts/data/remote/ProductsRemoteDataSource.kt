package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.data.models.ResponseProducts

interface ProductsRemoteDataSource {
    /**
     * Возвращает ResponseProducts
     *
     * @param skip пропускает n количество элементов
     * @param limit выводит n количество элементов
     *
     * @return ResponseProducts
     */
    suspend fun getProducts(skip: Int, limit: Int): ResponseProducts

    /**
     * Возвращает ResponseProducts по запросу
     *
     * @param skip пропускает n количество элементов
     * @param limit выводит n количество элементов
     * @param query запрос
     *
     * @return ResponseProducts
     */
    suspend fun getProductsByQuery(skip: Int, limit: Int, query: String): ResponseProducts

    /**
     * Возвращает отдельный Product по его id
     *
     * @param id id товара
     *
     * @return Product
     */
    suspend fun getSingleProduct(id: Int): Product

    /**
     * Возвращает список категорий
     *
     * @return List<String>
     */
    suspend fun getCategories(): List<String>

    /**
     * Возвращает ResponseProducts по определённой категории
     *
     * @param nameCategory название категории
     * @param limit выводит n количество элементов
     * @param query запрос
     *
     * @return ResponseProducts
     */
    suspend fun getProductsByCategory(nameCategory: String, skip: Int, limit: Int): ResponseProducts
}