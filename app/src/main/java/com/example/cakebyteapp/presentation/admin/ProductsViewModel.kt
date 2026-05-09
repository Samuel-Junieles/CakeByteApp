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
    private val _selectedCategory = MutableStateFlow("Todos")

    val products: StateFlow<List<ProductEntity>> = combine(
        productRepository.getAllProducts(),
        _searchQuery,
        _selectedStatus,
        _selectedCategory
    ) { allProducts, query, status, category ->
        allProducts.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true)
            val matchesStatus = status == "Todos" || product.status.equals(status, ignoreCase = true)
            val matchesCategory = category == "Todos" || product.category.equals(category, ignoreCase = true)
            matchesQuery && matchesStatus && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChanged(status: String) {
        _selectedStatus.value = status
    }

    fun onCategoryFilterChanged(category: String) {
        _selectedCategory.value = category
    }

    fun getProductById(productId: Int): Flow<ProductEntity?> {
        return productRepository.getAllProducts().map { list ->
            list.find { it.id == productId }
        }
    }

    fun saveProduct(
        id: Int = 0,
        name: String,
        description: String,
        price: Double,
        category: String,
        stock: Int,
        status: String = "Activo"
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
                    status = status
                )
                if (id == 0) {
                    // Para insertar uno nuevo, aseguramos que el ID sea 0
                    productRepository.insertProduct(product.copy(id = 0))
                } else {
                    productRepository.updateProduct(product)
                }
                android.util.Log.d("PRODUCT_DEBUG", "Producto guardado con éxito: $name")
            } catch (e: Exception) {
                android.util.Log.e("PRODUCT_DEBUG", "Error al guardar producto: ${e.message}")
            }
        }
    }

    fun addSampleProducts() {
        viewModelScope.launch {
            productRepository.insertProduct(ProductEntity(name = "Tarta de fresas", price = 18000.0, stock = 5, status = "Activo", description = "Deliciosa tarta con fresas frescas y crema."))
            productRepository.insertProduct(ProductEntity(name = "Chocobrownie", price = 12000.0, stock = 10, status = "Activo", description = "Brownie melcochudo con mucho chocolate."))
            productRepository.insertProduct(ProductEntity(name = "Red velvet", price = 22000.0, stock = 3, status = "Activo", description = "Pastel terciopelo rojo con crema de queso."))
        }
    }
}
