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
    private val _selectedCategory = MutableStateFlow("Todos")
    private val _selectedStatus = MutableStateFlow("Todos")

    val products: StateFlow<List<ProductEntity>> = combine(
        productRepository.getAllProducts(),
        _searchQuery,
        _selectedCategory,
        _selectedStatus
    ) { allProducts, query, category, status ->
        allProducts.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true)
            val matchesCategory = category == "Todos" || product.category.equals(category, ignoreCase = true)
            val matchesStatus = status == "Todos" || product.status.equals(status, ignoreCase = true)
            matchesQuery && matchesCategory && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Cargar productos iniciales si la DB está vacía
        viewModelScope.launch {
            productRepository.getAllProducts().first().let { 
                if (it.isEmpty()) {
                    addSampleProducts()
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryFilterChanged(category: String) {
        _selectedCategory.value = category
    }

    fun onStatusFilterChanged(status: String) {
        _selectedStatus.value = status
    }

    fun getProductById(id: Int): Flow<ProductEntity?> = productRepository.getProductById(id)

    fun saveProduct(
        id: Int = 0,
        name: String,
        description: String,
        price: Double,
        category: String,
        stock: Int,
        status: String = "Activo",
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            try {
                val product = ProductEntity(
                    id = id,
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    stock = stock,
                    status = status,
                    imageUrl = imageUrl
                )
                productRepository.insertProduct(product)
            } catch (e: Exception) {
                // Log error
            }
        }
    }

    private fun addSampleProducts() {
        viewModelScope.launch {
            val samples = listOf(
                ProductEntity(name = "Torta de Chocolate", description = "Deliciosa torta de cacao", price = 25000.0, category = "Pasteles", stock = 10, imageUrl = "torta_de_chocolate", status = "Activo"),
                ProductEntity(name = "Torta de Vainilla", description = "Torta suave de vainilla", price = 22000.0, category = "Pasteles", stock = 8, imageUrl = "torta_de_vainilla", status = "Activo"),
                ProductEntity(name = "Cheesecake Mora", description = "Base de galleta y mora", price = 18000.0, category = "Pasteles", stock = 5, imageUrl = "cheesecake_de_mora", status = "Activo"),
                ProductEntity(name = "Galletas Chocolate", description = "Paquete x6 galletas", price = 5000.0, category = "Galletas", stock = 20, imageUrl = "galletas_de_chocolate", status = "Activo"),
                ProductEntity(name = "Pan Artesanal", description = "Recién horneado", price = 3000.0, category = "Pan", stock = 15, imageUrl = "carrot_torta", status = "Activo")
            )
            samples.forEach { productRepository.insertProduct(it) }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }
}
