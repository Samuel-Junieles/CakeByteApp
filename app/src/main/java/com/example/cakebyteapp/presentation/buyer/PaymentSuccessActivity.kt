package com.example.cakebyteapp.presentation.buyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityPaymentSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupReceipt()
        setupListeners()
    }

    private fun setupReceipt() {
        binding.rowStatus.tvLabel.text = getString(R.string.label_transaction_status)
        binding.rowStatus.tvValue.text = "22 de Septiembre"

        binding.rowMerchant.tvLabel.text = getString(R.string.label_merchant)
        binding.rowMerchant.tvValue.text = "CakeByte Bakery"

        binding.rowDate.tvLabel.text = getString(R.string.label_payment_date)
        binding.rowDate.tvValue.text = "22 de Septiembre"

        binding.rowTime.tvLabel.text = getString(R.string.label_payment_time)
        binding.rowTime.tvValue.text = "18:30"

        binding.rowId.tvLabel.text = getString(R.string.label_transaction_id)
        binding.rowId.tvValue.text = "#12345678910"

        binding.rowCus.tvLabel.text = getString(R.string.label_cus)
        binding.rowCus.tvValue.text = "1234567891"

        binding.rowConcept.tvLabel.text = getString(R.string.label_concept)
        binding.rowConcept.tvValue.text = "Pastel de Chocolate"
    }

    private fun setupListeners() {
        binding.btnBackHome.setOnClickListener {
            val intent = Intent(this, BuyerHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
