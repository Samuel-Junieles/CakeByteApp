package com.example.cakebyteapp.presentation.vendor

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

class VendorProductAdapter(
    private val onEdit: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, VendorProductAdapter.VendorProductViewHolder>(VendorProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorProductViewHolder {
        val binding = ItemVendedorProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VendorProductViewHolder(private val binding: ItemVendedorProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.tvProductName.text = product.name
            
            val colombianLocale = Locale("es", "CO")
            val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
            binding.tvProductPrice.text = currencyFormatter.format(product.price)

            // Asignación de imagen priorizando el campo imageUrl guardado
            val imageName = product.imageUrl ?: ""
            val context = binding.root.context
            val resId = if (imageName.isNotEmpty()) {
                context.resources.getIdentifier(imageName, "drawable", context.packageName)
            } else {
                // Lógica de respaldo si no hay imagen seleccionada
                when {
                    product.name.contains("Chocolate", true) -> R.drawable.torta_de_chocolate
                    product.name.contains("Vainilla", true) -> R.drawable.torta_de_vainilla
                    product.name.contains("Red velvet", true) -> R.drawable.red_velvet_torta
                    else -> R.drawable.torta_de_chocolate
                }
            }
            
            if (resId != 0) {
                binding.ivProduct.setImageResource(resId)
            } else {
                binding.ivProduct.setImageResource(R.drawable.torta_de_chocolate)
            }

            binding.btnEdit.setOnClickListener { onEdit(product) }
            binding.root.setOnClickListener { onEdit(product) }
        }
    }

    class VendorProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean = oldItem == newItem
    }
}
