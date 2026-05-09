package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.OrderDao
import com.example.cakebyteapp.data.local.entity.OrderEntity
import com.example.cakebyteapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    override fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()
    override suspend fun insertOrder(order: OrderEntity) = orderDao.insertOrder(order)
    override suspend fun updateOrder(order: OrderEntity) = orderDao.updateOrder(order)
    override suspend fun deleteOrder(order: OrderEntity) = orderDao.deleteOrder(order)
}
