package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.CartDao
import com.example.cakebyteapp.data.local.entity.CartItemEntity
import com.example.cakebyteapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {
    override fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    override suspend fun addToCart(item: CartItemEntity) = cartDao.insertCartItem(item)

    override suspend fun updateQuantity(item: CartItemEntity) = cartDao.updateCartItem(item)

    override suspend fun removeFromCart(item: CartItemEntity) = cartDao.deleteCartItem(item)

    override suspend fun clearCart() = cartDao.clearCart()
}
