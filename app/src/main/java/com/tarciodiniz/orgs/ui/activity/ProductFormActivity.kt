package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityProductFormBinding
import com.tarciodiniz.orgs.extensions.PRODUCT_KEY
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.repository.ProductRepository
import com.tarciodiniz.orgs.ui.dialog.FormImageDialog
import com.tarciodiniz.orgs.webclient.dto.ProductDto
import com.tarciodiniz.orgs.webclient.product.ProductWebServices
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ProductFormActivity : ActivityBaseUser() {

    private val binding by lazy {
        ActivityProductFormBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var idProduct: String? = null

    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
    }

    private val repository by lazy {
        ProductRepository(productDao, ProductWebServices())
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
                .collect {

                }
        }
    }

    private fun tryToLookForProduct() {
        intent.getStringExtra(PRODUCT_KEY)?.let { id ->
            lifecycleScope.launch {
                repository.getFromID(id)?.let {
                    idProduct = id
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
                    val newProductDto = ProductDto(
                        id = newProduct.id,
                        name = newProduct.name,
                        description = newProduct.description,
                        valueProduct = newProduct.value,
                        image = newProduct.image,
                        userID = newProduct.userID
                    )
                    if (idProduct != null) {
                        newProduct.apply {
                            repository.update(newProductDto)
                        }

                    } else {
                        newProduct.apply {
                            repository.save(newProductDto)
                        }
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

        if (idProduct == null) {
            return Product(
                name = name,
                description = description,
                value = value,
                image = url,
                userID = userID

            )
        } else {
            return Product(
                id = idProduct!!,
                name = name,
                description = description,
                value = value,
                image = url,
                userID = userID

            )
        }
    }
}