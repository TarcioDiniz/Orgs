package com.tarciodiniz.orgs.model

import androidx.room.Entity
import java.math.BigDecimal

@Entity
data class Produto(
    val name: String,
    val description: String,
    val value: BigDecimal,
    val image: String? = null
): java.io.Serializable
