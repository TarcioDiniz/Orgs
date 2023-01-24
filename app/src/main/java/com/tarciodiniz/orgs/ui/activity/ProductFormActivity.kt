package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.model.Produto
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureButtonToSave()
    }

    private fun configureButtonToSave() {
        val dao = ProductsDao()
        val fieldButton = findViewById<Button>(R.id.activity_toSave)
        fieldButton.setOnClickListener {
            val newProduct = createProduct()
            dao.setProduct(newProduct)
            finish()
        }
    }

    private fun createProduct(): Produto {
        val fieldName = findViewById<EditText>(R.id.activity_name)
        val fieldDescription = findViewById<EditText>(R.id.activity_description)
        val fieldValue = findViewById<EditText>(R.id.activity_value)

        val name = fieldName.text.toString()
        val description = fieldDescription.text.toString()
        val valueText = fieldValue.text.toString()

        val value = if (valueText.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valueText)
        }

        return Produto(
            name = name, description = description, value = value
        )
    }
}