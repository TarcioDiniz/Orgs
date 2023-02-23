package com.tarciodiniz.orgs.webclient.services

import com.tarciodiniz.orgs.webclient.dto.UserDTO
import retrofit2.http.GET

interface UserServices {
    @GET("Users")
    suspend fun getUsers(): List<UserDTO>
}