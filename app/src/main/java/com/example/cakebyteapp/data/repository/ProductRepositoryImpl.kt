package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.ProductDao
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    override fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    override fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    override suspend fun insertProduct(product: ProductEntity) = productDao.insertProduct(product)

    override suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)

    override suspend fun deleteProduct(product: ProductEntity) = productDao.deleteProduct(product)
}
