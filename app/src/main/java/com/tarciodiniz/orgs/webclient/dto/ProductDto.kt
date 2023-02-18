package com.tarciodiniz.orgs.webclient.dto

import com.squareup.moshi.Json
import java.math.BigDecimal

data class ProductDto(
    @Json(name = "id")
    val id: Long = 0L,
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "valueProduct")
    val valueProduct: BigDecimal,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "userID")
    val userID: String? = null
)
