package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.databinding.ActivityProductFormBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Produto
import com.tarciodiniz.orgs.ui.dialog.FormImageDialog
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProductFormBinding.inflate(layoutInflater)
    }

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Register Product"
        configureButtonToSave()
        binding.activityImagem.setOnClickListener {
            FormImageDialog(this).showDialog(url) { image ->
                url = image
                binding.activityImagem.tryToLoad(url)
            }
        }

    }

    private fun configureButtonToSave() {
        val dao = ProductsDao()
        val fieldButton = binding.activityToSave
        fieldButton.setOnClickListener {
            val newProduct = createProduct()
            dao.setProduct(newProduct)
            finish()
        }
    }

    private fun createProduct(): Produto {
        val fieldName = binding.activityName
        val fieldDescription = binding.activityDescription
        val fieldValue = binding.activityValue

        val name = fieldName.text.toString()
        val description = fieldDescription.text.toString()
        val valueText = fieldValue.text.toString()

        val value = if (valueText.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valueText)
        }

        return Produto(
            name = name, description = description, value = value, image = url
        )
    }
}