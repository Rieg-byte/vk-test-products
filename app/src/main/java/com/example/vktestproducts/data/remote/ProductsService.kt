package com.example.vktestproducts.data.remote

import com.example.vktestproducts.data.models.Product
import com.example.vktestproducts.data.models.ResponseProducts
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsService {
    /**
     * GET-запрос. Возвращает ResponseProducts
     *
     * @param skip пропускает n количество элементов
     * @param limit выводит n количество элементов
     *
     * @return ResponseProducts
     */
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ResponseProducts

    /**
     * GET-запрос. Возвращает ResponseProducts по запросу
     *
     * @param skip пропускает n количество элементов
     * @param limit выводит n количество элементов
     * @param query запрос
     *
     * @return ResponseProducts
     */
    @GET("products/search")
    suspend fun getProductsByQuery(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("q") query: String
    ): ResponseProducts

    /**
     * GET-запрос. Возвращает отдельный Product по его id
     *
     * @param id id товара
     *
     * @return Product
     */
    @GET("products/{id}")
    suspend fun getSingleProduct(
        @Path("id") id: Int
    ): Product

    /**
     * GET-запрос. Возвращает список категорий
     *
     * @return List<String>
     */
    @GET("products/categories")
    suspend fun getCategories(): List<String>

    /**
     * GET-запрос. Возвращает ResponseProducts по определённой категории
     *
     * @param nameCategory название категории
     * @param limit выводит n количество элементов
     * @param query запрос
     *
     * @return ResponseProducts
     */
    @GET("/products/category/{nameCategory}")
    suspend fun getProductsByCategory(
        @Path("nameCategory") nameCategory: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): ResponseProducts
}