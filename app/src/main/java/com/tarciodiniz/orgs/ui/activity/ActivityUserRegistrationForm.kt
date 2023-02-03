package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityUserRegistrationFormBinding
import com.tarciodiniz.orgs.extensions.toHash
import com.tarciodiniz.orgs.model.User
import kotlinx.coroutines.launch

class ActivityUserRegistrationForm : AppCompatActivity(R.layout.activity_user_registration_form) {

    private val binding by lazy {
        ActivityUserRegistrationFormBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        AppDatabase.getInstance(this).UserDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Register User"
        setContentView(binding.root)
        configureRegisterButton()

    }

    private fun configureRegisterButton() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val newUser = createUser()
            register(newUser)
        }
    }

    private fun register(user: User) {
        lifecycleScope.launch {
            try {
                dao.save(user)
                finish()
            } catch (e: Exception) {
                Log.e("user_registration_form", "Register Button", e)
                Toast.makeText(
                    this@ActivityUserRegistrationForm,
                    "Failed to register the user",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createUser(): User {
        val username = binding.activityFormularioCadastroUsuario.text.toString()
        val name = binding.activityFormularioCadastroNome.text.toString()
        val password = binding.activityFormularioCadastroSenha.text.toString().toHash()
        return User(username, name, password)
    }

}