package com.example.cakebyteapp.presentation.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.data.local.entity.CartItemEntity
import com.example.cakebyteapp.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemEntity>> = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.productPrice * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun removeItem(item: CartItemEntity) {
        viewModelScope.launch {
            cartRepository.removeFromCart(item)
        }
    }

    fun updateQuantity(item: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item.copy(quantity = newQuantity))
        }
    }
}
