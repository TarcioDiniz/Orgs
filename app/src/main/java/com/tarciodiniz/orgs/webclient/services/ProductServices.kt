package com.tarciodiniz.orgs.webclient.services

import com.tarciodiniz.orgs.webclient.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductServices {

    @GET("Product")
    suspend fun getProducts(): Response<ProductsResponse>

}