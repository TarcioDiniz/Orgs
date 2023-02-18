package com.tarciodiniz.orgs.webclient.product

import android.util.Log
import com.tarciodiniz.orgs.extensions.mapProductDtoToProduct
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.InitializerRetrofit

private const val TAG = "ProductAPI"

class ProductWebServices {

    suspend fun getProducts(): List<Product> {
        try {
            val response = InitializerRetrofit().productServices.getProducts()
            if (response.isSuccessful) {
                val productList = response.body()?.content?.map { dto ->
                    mapProductDtoToProduct(dto)
                }
                Log.i(TAG, "onCreate: $productList")
                return productList ?: emptyList()
            } else {
                Log.e(TAG, "onCreate: failed to get products")
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: failed to get products", e)
        }
        return emptyList()
    }

}
