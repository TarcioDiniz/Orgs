package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityProductViewBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ProductView : AppCompatActivity() {
    private lateinit var product: Product
    private val binding by lazy {
        ActivityProductViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        disable active appbar
//        val actionBar = supportActionBar
//        actionBar!!.hide()
        tryToLoadProduct()

    }

    private fun tryToLoadProduct() {
        val productLoad = intent.getParcelableExtra<Product>("product")
        if (productLoad != null) {
            product = productLoad
        }
        productLoad?.let { fillInFields(it) }

    }

    private fun fillInFields(product: Product) {
        val name = binding.productNameView
        val description = binding.productDescriptionView
        val value = binding.productButtonView

        Log.i("binding fill", name.toString())


        name.text = product.name
        description.text = product.description
        value.text = formatForCurrency(product.value)

        if (product.image != null) {
            binding.productImageView.tryToLoad(product.image)
        }

        setContentView(binding.root)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::product.isInitialized) {
            val db = AppDatabase.getInstance(this)
            val productDao = db.productDao()
            when (item.itemId) {
                R.id.remove_product_details_menu -> {
                    productDao.delete(product)
                    finish()
                }
                R.id.edit_product_details_menu -> {
                    Intent(this, ProductFormActivity::class.java).apply {
                        putExtra("product", product)
                        startActivity(this)
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun formatForCurrency(value: BigDecimal): String {
        val formatter: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatter.format(value)
    }

}