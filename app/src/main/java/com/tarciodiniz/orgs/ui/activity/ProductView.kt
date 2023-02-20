package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityProductViewBinding
import com.tarciodiniz.orgs.extensions.PRODUCT_KEY
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ProductView : AppCompatActivity() {
    private var productId: String? = null
    private var product: Product? = null
    private val binding by lazy {
        ActivityProductViewBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        disable active appbar
//        val actionBar = supportActionBar
//        actionBar!!.hide()
        tryToLoadProduct()

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            productId?.let { id ->
                product = productDao.getFromID(id)
            }
            product?.let {
                fillInFields(it)
            } ?: finish()
        }
    }

    private fun tryToLoadProduct() {
        intent.getStringExtra("product")?.let { id ->
            productId = id
        }


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

        when (item.itemId) {
            R.id.remove_product_details_menu -> {
                lifecycleScope.launch {
                    product?.let { productDao.delete(it) }
                }
                finish()
            }
            R.id.edit_product_details_menu -> {
                invokeActivity(ProductFormActivity::class.java){
                    putExtra(PRODUCT_KEY, product?.id)
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