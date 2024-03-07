package com.example.vktestproducts.di.modules

import com.example.vktestproducts.data.remote.ProductsRemoteDataSource
import com.example.vktestproducts.data.remote.ProductsRemoteDataSourceImpl
import com.example.vktestproducts.data.repository.products.ProductsRepository
import com.example.vktestproducts.data.repository.products.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsModule {
    @Binds
    abstract fun provideProductsRemoteDataSource(
        productsRemoteDataSource: ProductsRemoteDataSourceImpl
    ): ProductsRemoteDataSource

    @Binds
    abstract fun provideProductsRepository(
        productsRepository: ProductsRepositoryImpl
    ): ProductsRepository
}