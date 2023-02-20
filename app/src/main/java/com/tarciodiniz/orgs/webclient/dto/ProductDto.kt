package com.tarciodiniz.orgs.webclient.dto

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.util.UUID

data class ProductDto(
    @Json(name = "id")
    val id: String,
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
