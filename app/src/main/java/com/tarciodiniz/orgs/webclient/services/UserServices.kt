package com.tarciodiniz.orgs.webclient.services

import com.tarciodiniz.orgs.webclient.dto.SetUserDto
import com.tarciodiniz.orgs.webclient.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserServices {
    @GET("Users")
    suspend fun getUsers(): List<UserDTO>

    @POST("Users")
    suspend fun setUsers(@Body user: SetUserDto): Response<Unit>

}