package com.tarciodiniz.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.model.Produto
import java.math.BigDecimal
import java.math.BigInteger

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fieldButton = findViewById<Button>(R.id.toSave)
        fieldButton.setOnClickListener {
            val fieldName = findViewById<EditText>(R.id.name)
            val fieldDescription = findViewById<EditText>(R.id.description)
            val fieldValue = findViewById<EditText>(R.id.value)

            val name = fieldName.text.toString()
            val description = fieldDescription.text.toString()
            val valueText = fieldValue.text.toString()

            val value = if (valueText.isBlank()){
                BigDecimal.ZERO
            } else{
              BigDecimal(valueText)
            }

            val newProduct = Produto(
                name = name,
                description = description,
                value = value
            )

            Log.i("ProductFormActivity", "onCreate: $newProduct")
            val dao = ProductsDao()
            dao.setProduct(newProduct)
            Log.i("ProductFormActivity", "onCreate: ${dao.getProduct()}")
        }




    }
}