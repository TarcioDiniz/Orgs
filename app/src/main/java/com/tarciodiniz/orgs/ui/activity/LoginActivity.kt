package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityLoginBinding
import com.tarciodiniz.orgs.extensions.LOGGED_USER_KEY
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.extensions.toHash
import com.tarciodiniz.orgs.model.User
import com.tarciodiniz.orgs.preferences.dataStore
import com.tarciodiniz.orgs.webclient.user.UserWebServices
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val userDao by lazy {
        AppDatabase.getInstance(this).UserDao()
    }

    private val userWebServices by lazy {
        UserWebServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val actionBar = supportActionBar
        actionBar!!.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            val listuser = userWebServices.getUsers()
            Log.i("UserAPI", listuser.toString())
        }
        login()
        signUp()
    }

    private fun signUp() {
        binding.activityLoginButtonSignUp.setOnClickListener {
            invokeActivity(ActivityUserRegistrationForm::class.java)
        }
    }

    private fun login() {
        binding.activityLoginButtonLog.setOnClickListener {
            val username = binding.activityLoginUsername.text.toString()
            val password = binding.activityLoginPassword.text.toString().toHash()
            authenticate(username, password)
        }
    }

    private fun authenticate(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val users = userWebServices.getUsers()
                val user = users.firstOrNull { it.id == username && it.password == password }
                if (user == null) {
                    binding.textInputLayoutPassword.error = "Username or Password is INVALID"
                    return@launch
                }
                try {
                    userDao.save(User(user.id, user.name, user.password))
                } catch (e: Exception) {
                    Log.e("UserAPI", "Logged in user has already been added to the database", e)
                }
            } catch (e: Exception) {
                Log.e("ErrorLogin", "Error authenticating user", e)
            } finally {
                userDao.authentic(username, password)?.let { authenticatedUser ->
                    dataStore.edit { preferences ->
                        preferences[LOGGED_USER_KEY] = authenticatedUser.id
                    }
                    invokeActivity(ListProductsActivity::class.java)
                    binding.textInputLayoutPassword.error = null
                    finish()
                } ?: run {
                    binding.textInputLayoutPassword.error = "Username or Password is INVALID"
                }
            }
        }
    }


}