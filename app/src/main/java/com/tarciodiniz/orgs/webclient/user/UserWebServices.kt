package com.tarciodiniz.orgs.webclient.user

import android.util.Log
import com.tarciodiniz.orgs.extensions.mapUserDtoToUser
import com.tarciodiniz.orgs.model.User
import com.tarciodiniz.orgs.webclient.InitializerRetrofit


private const val TAG = "UserAPI"
private const val SUCCESS_MESSAGE = "onCreate: Success"
private const val FAILED_MESSAGE = "onCreate: failed to get users"

class UserWebServices {

    private val userServices by lazy {
        InitializerRetrofit().userServices
    }

    suspend fun getUsers(): List<User> {
        return try {
            Log.i(TAG, SUCCESS_MESSAGE)
            userServices.getUsers().map { dto ->
                mapUserDtoToUser(dto)
            }
        } catch (e: Exception) {
            Log.e(TAG, FAILED_MESSAGE, e)
            emptyList()
        }
    }


}