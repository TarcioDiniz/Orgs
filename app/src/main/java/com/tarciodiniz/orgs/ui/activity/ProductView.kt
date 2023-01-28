package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tarciodiniz.orgs.databinding.ActivityProductViewBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ProductView : AppCompatActivity() {
    private val binding by lazy {
        ActivityProductViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar!!.hide()
        val product = intent.getSerializableExtra("product") as Product

        val name = binding.productNameView
        val description = binding.productDescriptionView
        val value = binding.productButtonView

        name.text = product.name
        description.text = product.description
        value.text = formatForCurrency(product.value)

        if (product.image != null){
            binding.productImageView.tryToLoad(product.image)
        }

        setContentView(binding.root)

    }

    private fun formatForCurrency(value: BigDecimal): String {
        val formatter: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatter.format(value)
    }

}