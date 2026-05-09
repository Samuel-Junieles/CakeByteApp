package com.example.cakebyteapp.presentation.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.data.local.entity.CartItemEntity
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.domain.usecase.AddToCartUseCase
import com.example.cakebyteapp.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _product = MutableStateFlow<ProductEntity?>(null)
    val product = _product.asStateFlow()

    private val _selectedSize = MutableStateFlow("20 cm")
    val selectedSize = _selectedSize.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity = _quantity.asStateFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            getProductByIdUseCase(id).collect {
                _product.value = it
            }
        }
    }

    fun selectSize(size: String) {
        _selectedSize.value = size
    }

    fun addToCart(note: String) {
        val currentProduct = _product.value ?: return
        viewModelScope.launch {
            val cartItem = CartItemEntity(
                productId = currentProduct.id,
                productName = currentProduct.name,
                productPrice = currentProduct.price,
                quantity = _quantity.value,
                size = _selectedSize.value,
                note = note,
                imageUrl = currentProduct.imageUrl
            )
            addToCartUseCase(cartItem)
        }
    }
}
