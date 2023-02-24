package com.tarciodiniz.orgs.webclient.user

import android.util.Log
import com.tarciodiniz.orgs.extensions.mapUserDtoToUser
import com.tarciodiniz.orgs.model.User
import com.tarciodiniz.orgs.webclient.InitializerRetrofit
import com.tarciodiniz.orgs.webclient.dto.SetUserDto


private const val TAG = "UserAPI"
private const val SUCCESS_MESSAGE = "onCreate: Success"
private const val FAILED_GET_MESSAGE = "onCreate: failed to get users"
private const val FAILED_SET_MESSAGE = "onCreate: failed to set users"

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
            Log.e(TAG, FAILED_GET_MESSAGE, e)
            emptyList()
        }
    }

    suspend fun setUsers(user: SetUserDto): Boolean {
        return try {
            userServices.setUsers(user)
            Log.i(TAG, SUCCESS_MESSAGE)
            true
        }catch (e: Exception){
            Log.e(TAG, FAILED_SET_MESSAGE, e)
            false
        }
    }

}