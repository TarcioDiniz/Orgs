package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityLoginBinding
import com.tarciodiniz.orgs.extensions.LOGGED_USER_KEY
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.extensions.toHash
import com.tarciodiniz.orgs.preferences.dataStore
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val userDao by lazy {
        AppDatabase.getInstance(this).UserDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val actionBar = supportActionBar
        actionBar!!.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
            userDao.authentic(username, password)?.let { user ->
                dataStore.edit { preferences ->
                    preferences[LOGGED_USER_KEY] = user.id
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