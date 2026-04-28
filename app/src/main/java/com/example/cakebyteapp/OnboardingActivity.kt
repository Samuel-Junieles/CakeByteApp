package com.example.cakebyteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cakebyteapp.databinding.ActivityOnboardingBinding
import com.example.cakebyteapp.databinding.ItemOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.onboarding_1,
                getString(R.string.title_onboarding_1),
                getString(R.string.desc_onboarding_1)
            ),
            OnboardingItem(
                R.drawable.onboarding_2,
                getString(R.string.title_onboarding_2),
                getString(R.string.desc_onboarding_2)
            ),
            OnboardingItem(
                R.drawable.onboarding_3,
                getString(R.string.title_onboarding_3),
                getString(R.string.desc_onboarding_3)
            )
        )

        binding.viewPager.adapter = OnboardingAdapter(onboardingItems) {
            // Acción al hacer clic en "Empieza a comprar"
            // Por ejemplo: ir al Login o Home
        }
    }

    data class OnboardingItem(val image: Int, val title: String, val description: String)

    class OnboardingAdapter(
        private val items: List<OnboardingItem>,
        private val onStartClick: () -> Unit
    ) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

        inner class OnboardingViewHolder(val binding: ItemOnboardingBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
            val binding = ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return OnboardingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
            val item = items[position]
            holder.binding.ivOnboarding.setImageResource(item.image)
            holder.binding.tvTitle.text = item.title
            holder.binding.tvDescription.text = item.description

            // Mostrar botón solo en la última pantalla
            if (position == items.size - 1) {
                holder.binding.btnStart.visibility = View.VISIBLE
                holder.binding.btnStart.setOnClickListener { onStartClick() }
            } else {
                holder.binding.btnStart.visibility = View.GONE
            }

            // Update indicators
            holder.binding.indicator1.setBackgroundResource(if (position == 0) R.color.salmon_primary else R.color.indicator_inactive)
            holder.binding.indicator2.setBackgroundResource(if (position == 1) R.color.salmon_primary else R.color.indicator_inactive)
            holder.binding.indicator3.setBackgroundResource(if (position == 2) R.color.salmon_primary else R.color.indicator_inactive)
        }

        override fun getItemCount(): Int = items.size
    }
}