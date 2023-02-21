package com.tarciodiniz.orgs.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.math.BigDecimal
import java.util.*

@Entity
data class Product(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "valueProduct")
    val value: BigDecimal,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "userID")
    val userID: String? = null,
    @ColumnInfo(defaultValue = "0")
    val syncNow: Boolean = false
)
