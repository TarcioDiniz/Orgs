package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.extensions.LOGGED_USER_KEY
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.model.User
import com.tarciodiniz.orgs.preferences.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class ActivityBaseUser : AppCompatActivity() {

    private val userDao by lazy {
        AppDatabase.getInstance(this).UserDao()
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected val user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            checkLoggedInUser()
        }
    }

    private suspend fun checkLoggedInUser() {
        dataStore.data.collect { preferences ->
            preferences[LOGGED_USER_KEY]?.let { userID ->
                fetchUser(userID)
            } ?: goToLogin()
        }
    }

    private suspend fun fetchUser(userID: String): User? {
        return userDao
            .searchUserByID(userID)
            .firstOrNull().also {
                _user.value = it
            }
    }

    private fun goToLogin() {
        invokeActivity(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    protected suspend fun logoutUser() {
        dataStore.edit { preferences ->
            preferences.remove(LOGGED_USER_KEY)
        }
    }
}