package com.tarciodiniz.orgs.repository

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.database.dao.ProductDao
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.dto.ProductDto
import com.tarciodiniz.orgs.webclient.product.ProductWebServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class ProductRepository(
    private val dao: ProductDao,
    private val productWebServices: ProductWebServices
) {

    fun getProducts(): Flow<List<Product>> {
        return dao.getAll()
    }

    private suspend fun updateAll() {
        productWebServices.getProducts().let { productResponse ->
            productResponse.map { product ->
                val productSync = product.copy(syncNow = true)
                dao.save(productSync)
            }
        }
    }

    suspend fun searchAllFromUser(userID: String): Flow<List<Product>> {
        productWebServices.getProducts().let { productResponse ->
            syncNow()
            productResponse.map { product ->
                if (product.userID == userID) {
                    Log.i("webapi", product.toString())
                    product.let {
                        val productSync = it.copy(syncNow = true)
                        dao.save(productSync)
                    }
                }
            }
        }
        return dao.searchAllFromUser(userID)
    }

    suspend fun getFromID(id: String): Product? {
        return dao.getFromID(id)
    }

    suspend fun update(product: Product) {
        dao.update(product)
        product.apply {
            if (productWebServices.save(
                    ProductDto(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        valueProduct = product.value,
                        image = product.image,
                        userID = product.userID
                    )
                )
            ) {
                val productSync = product.copy(syncNow = true)
                dao.update(productSync)
            }
        }
    }

    suspend fun save(product: Product) {
        dao.save(product)
        product.apply {
            if (productWebServices.save(
                    ProductDto(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        valueProduct = product.value,
                        image = product.image,
                        userID = product.userID
                    )
                )
            ) {
                val productSync = product.copy(syncNow = true)
                dao.update(productSync)
            }
        }
    }

    private suspend fun syncNow(){
        val productsNotSync = dao.getAllNotSync().first()
        productsNotSync.forEach { productNotSync ->
            save(productNotSync)
        }
        updateAll()
    }

}