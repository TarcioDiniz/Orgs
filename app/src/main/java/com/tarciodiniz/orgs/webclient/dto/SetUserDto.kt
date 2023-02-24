package com.tarciodiniz.orgs.webclient.dto

import com.squareup.moshi.Json

data class SetUserDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String
)