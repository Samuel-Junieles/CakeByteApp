package com.example.cakebyteapp.presentation.vendor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cakebyteapp.R
import com.example.cakebyteapp.data.local.entity.OrderEntity
import com.example.cakebyteapp.databinding.ItemOrderBinding
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private val onComplete: (OrderEntity) -> Unit,
    private val onDelete: (OrderEntity) -> Unit
) : ListAdapter<OrderEntity, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderEntity) {
            val context = binding.root.context
            binding.tvOrderTitle.text = context.getString(R.string.label_order_id, order.id, order.customerName)
            binding.tvOrderDetails.text = order.itemsSummary
            
            val colombianLocale = Locale("es", "CO")
            val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
            binding.tvOrderPrice.text = currencyFormatter.format(order.totalPrice)

            binding.tvStatusBadge.text = order.status
            if (order.status == "Pendiente") {
                binding.tvStatusBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.percentage_red)
                binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.text_red))
            } else {
                binding.tvStatusBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.percentage_green)
                binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.text_green))
            }

            binding.ivCheck.setOnClickListener { onComplete(order) }
            binding.btnDelete.setOnClickListener { onDelete(order) }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<OrderEntity>() {
        override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean = oldItem == newItem
    }
}
