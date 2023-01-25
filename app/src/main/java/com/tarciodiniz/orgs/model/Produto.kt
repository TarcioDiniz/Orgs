package com.tarciodiniz.orgs.model

import java.math.BigDecimal

data class Produto(
    val name: String,
    val description: String,
    val value: BigDecimal,
    val image: String? = null
): java.io.Serializable
