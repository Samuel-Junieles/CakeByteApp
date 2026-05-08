package com.example.cakebyteapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow("Todos")

    val products: StateFlow<List<ProductEntity>> = combine(
        productRepository.getAllProducts(),
        _searchQuery,
        _selectedStatus
    ) { allProducts, query, status ->
        allProducts.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true)
            val matchesStatus = status == "Todos" || product.status.equals(status, ignoreCase = true)
            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChanged(status: String) {
        _selectedStatus.value = status
    }

    fun addSampleProducts() {
        viewModelScope.launch {
            productRepository.insertProduct(ProductEntity(name = "Tarta de fresas", price = 18.0, stock = 5, status = "Activo"))
            productRepository.insertProduct(ProductEntity(name = "Chocobrownie", price = 12.0, stock = 0, status = "Suspendido"))
            productRepository.insertProduct(ProductEntity(name = "Red velvet", price = 22.0, stock = 0, status = "Suspendido"))
        }
    }
}
