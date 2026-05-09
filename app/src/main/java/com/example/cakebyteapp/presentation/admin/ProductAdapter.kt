package com.example.cakebyteapp.presentation.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cakebyteapp.R
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onEdit: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            val context = binding.root.context
            binding.tvProductName.text = product.name
            
            val colombianLocale = Locale("es", "CO")
            val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
            val priceFormatted = currencyFormatter.format(product.price)
            binding.tvProductInfo.text = context.getString(R.string.label_stock_info, priceFormatted, product.stock)
            
            // Estilo del badge según estado
            if (product.status == "Activo") {
                binding.tvStatusBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.chip_active_bg)
                binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.text_green))
                binding.tvStatusBadge.text = context.getString(R.string.status_active)
            } else {
                binding.tvStatusBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.chip_suspended_bg)
                binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.text_red))
                binding.tvStatusBadge.text = context.getString(R.string.status_suspended)
            }

            binding.root.setOnClickListener { onEdit(product) }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem == newItem
    }
}
