package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityProductFormBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.ui.dialog.FormImageDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ProductFormActivity : ActivityBaseUser() {

    private val binding by lazy {
        ActivityProductFormBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var idProduct = 0L

    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
    }

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
        tryToLookForProduct()
        lifecycleScope.launch {
            user
                .filterNotNull()
                .collect{

            }
        }
    }

    private fun tryToLookForProduct() {
        intent.getParcelableExtra<Product>("product")?.let { productLoad ->
            lifecycleScope.launch {
                productDao.getFromID(productLoad.id)?.let {
                    idProduct = productLoad.id
                    title = "Change Product"
                    fillFields(it)
                }
            }
        }
    }

    private fun fillFields(productLoad: Product) {
        url = productLoad.image
        binding.activityImagem
            .tryToLoad(productLoad.image)
        binding.activityName
            .setText(productLoad.name)
        binding.activityDescription
            .setText(productLoad.description)
        binding.activityValue
            .setText(productLoad.value.toPlainString())
    }

    private fun configureButtonToSave() {
        val fieldButton = binding.activityToSave
        fieldButton.setOnClickListener {
            lifecycleScope.launch {
                user.value?.let { user ->
                    val newProduct = createProduct(user.id)
                    if (idProduct > 0L) {
                        productDao.update(newProduct)
                    } else {
                        productDao.save(newProduct)
                    }
                    finish()
                }
            }
        }
    }

    private fun createProduct(userID: String): Product {
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

        return Product(
            id = idProduct,
            name = name,
            description = description,
            value = value,
            image = url,
            userID = userID

        )
    }
}