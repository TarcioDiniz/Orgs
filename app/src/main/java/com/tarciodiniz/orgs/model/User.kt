package com.tarciodiniz.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val password: String
): Parcelable
