package com.tarciodiniz.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "valueProduct")
    val value: BigDecimal,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "userID")
    val userID: String? = null
) : Parcelable
