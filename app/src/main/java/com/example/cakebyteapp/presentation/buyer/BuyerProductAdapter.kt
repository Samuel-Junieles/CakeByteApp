package com.example.cakebyteapp.presentation.buyer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cakebyteapp.R
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.databinding.ItemVendedorProductBinding
import java.text.NumberFormat
import java.util.Locale

class BuyerProductAdapter(
    private val onClick: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, BuyerProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemVendedorProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemVendedorProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.tvProductName.text = product.name
            
            val colombianLocale = Locale("es", "CO")
            val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
            binding.tvProductPrice.text = currencyFormatter.format(product.price)

            // Lógica de imagen consistente con el vendedor
            val imageName = product.imageUrl ?: ""
            val context = binding.root.context
            val resId = if (imageName.isNotEmpty()) {
                context.resources.getIdentifier(imageName, "drawable", context.packageName)
            } else {
                when {
                    product.name.contains("Chocolate", true) -> R.drawable.torta_de_chocolate
                    product.name.contains("Vainilla", true) -> R.drawable.torta_de_vainilla
                    product.name.contains("Fresa", true) -> R.drawable.cheesecake_de_fresa
                    else -> R.drawable.torta_de_chocolate
                }
            }
            
            if (resId != 0) {
                binding.ivProduct.setImageResource(resId)
            } else {
                binding.ivProduct.setImageResource(R.drawable.torta_de_chocolate)
            }

            // Para el comprador, cambiamos el icono de editar por un "+"
            binding.btnEdit.setImageResource(android.R.drawable.ic_input_add)
            
            binding.root.setOnClickListener { onClick(product) }
            binding.btnEdit.setOnClickListener { onClick(product) }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem == newItem
    }
}
