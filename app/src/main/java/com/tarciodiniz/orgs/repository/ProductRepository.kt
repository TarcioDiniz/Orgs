package com.tarciodiniz.orgs.repository

import android.util.Log
import com.tarciodiniz.orgs.database.dao.ProductDao
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.dto.ProductDto
import com.tarciodiniz.orgs.webclient.product.ProductWebServices
import kotlinx.coroutines.flow.Flow


class ProductRepository(
    private val dao: ProductDao,
    private val productWebServices: ProductWebServices
) {

    fun getProducts(): Flow<List<Product>> {
        return dao.getAll()
    }

    suspend fun updateAll() {
        productWebServices.getProducts().let { productResponse ->
            productResponse.map { product ->
                dao.save(product)
            }
        }
    }

    suspend fun searchAllFromUser(userID: String): Flow<List<Product>> {
        productWebServices.getProducts().let { productResponse ->
            productResponse.map { product ->
                if (product.userID == userID) {
                    Log.i("webapi", product.toString())
                    dao.save(
                        Product(
                            id = product.id,
                            name = product.name,
                            description = product.description,
                            value = product.value,
                            image = product.image,
                            userID = product.userID
                        )
                    )
                }
            }
        }
        return dao.searchAllFromUser(userID)
    }

    suspend fun getFromID(id: String): Product? {
        return dao.getFromID(id)
    }

    suspend fun update(product: ProductDto) {
        product.apply {
            dao.update(
                Product(
                    id = product.id,
                    name = product.name,
                    description = product.description,
                    value = product.valueProduct,
                    image = product.image,
                    userID = product.userID
                )
            )
        }
        productWebServices.save(product)
    }

    suspend fun save(product: ProductDto) {
        product.apply {
            dao.save(
                Product(
                    id = product.id,
                    name = product.name,
                    description = product.description,
                    value = product.valueProduct,
                    image = product.image,
                    userID = product.userID
                )
            )
        }
        productWebServices.save(product)
    }

}