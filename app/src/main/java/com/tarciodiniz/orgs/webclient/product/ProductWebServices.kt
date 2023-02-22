package com.tarciodiniz.orgs.webclient.product

import android.util.Log
import com.tarciodiniz.orgs.extensions.mapProductDtoToProduct
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.InitializerRetrofit
import com.tarciodiniz.orgs.webclient.dto.ProductDto

private const val TAG = "ProductAPI"
private const val SUCCESS_MESSAGE = "onCreate: Success"
private const val FAILED_MESSAGE = "onCreate: failed to get products"
private const val FAILED_MESSAGE_DELETE = "onCreate: failed to delete product"

class ProductWebServices {

    private val productServices by lazy {
        InitializerRetrofit().productServices
    }

    suspend fun getProducts(): List<Product> {
        try {
            val response = productServices.getProducts()
            if (response.isSuccessful) {
                val productList = response.body()?.content?.map { dto ->
                    mapProductDtoToProduct(dto)
                }
                Log.i(TAG, SUCCESS_MESSAGE)
                return productList ?: emptyList()
            } else {
                Log.e(TAG, FAILED_MESSAGE)
            }
        } catch (e: Exception) {
            Log.e(TAG, FAILED_MESSAGE, e)
        }
        return emptyList()
    }

    suspend fun save(product: ProductDto): Boolean {
        try {
            val response = productServices.save(product.id, product)
            return response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, FAILED_MESSAGE, e)
        }
        return false
    }

    suspend fun delete(product: Product): Boolean {
        try {
            productServices.delete(product.id)
            return true
        } catch (e: Exception) {
            Log.e(TAG, FAILED_MESSAGE_DELETE, e)
        }
        return false
    }

}
