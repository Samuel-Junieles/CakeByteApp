package com.example.cakebyteapp.presentation.vendor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.databinding.ItemVendorProductBinding
import java.text.NumberFormat
import java.util.Locale

class VendorProductAdapter(
    private val onEdit: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, VendorProductAdapter.VendorProductViewHolder>(VendorProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorProductViewHolder {
        val binding = ItemVendorProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VendorProductViewHolder(private val binding: ItemVendorProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.tvProductName.text = product.name
            
            val colombianLocale = Locale("es", "CO")
            val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
            binding.tvProductPrice.text = currencyFormatter.format(product.price)

            // Here we would load image with Glide/Coil if we had a real URL
            // binding.ivProduct.load(product.imageUrl)

            binding.btnEdit.setOnClickListener { onEdit(product) }
            binding.root.setOnClickListener { onEdit(product) }
        }
    }

    class VendorProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem == newItem
    }
}
