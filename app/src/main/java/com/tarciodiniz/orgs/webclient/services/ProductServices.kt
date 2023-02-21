package com.tarciodiniz.orgs.webclient.services

import com.tarciodiniz.orgs.webclient.dto.ProductDto
import com.tarciodiniz.orgs.webclient.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductServices {

    @GET("Product")
    suspend fun getProducts(): Response<ProductsResponse>

    @PUT("Product/{id}")
    suspend fun save(
        @Path("id") id: String,
        @Body product: ProductDto
    ): Response<Unit>


}