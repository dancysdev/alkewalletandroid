package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.R
import com.example.alkewallet.controller.LoginController
import com.example.alkewallet.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val loginController by lazy {
        LoginController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los controles
        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etPassword =
            findViewById<EditText>(R.id.etPassword)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        val tvCreateAccount =
            findViewById<TextView>(R.id.tvCreateAccount)

        // Iniciar sesión
        btnLogin.setOnClickListener {

            val correo =
                etEmail.text.toString()

            val password =
                etPassword.text.toString()

            lifecycleScope.launch {

                val usuario =
                    loginController.login(
                        correo,
                        password
                    )

                if (usuario != null) {

                    SessionManager.iniciarSesion(
                        usuario
                    )

                    startActivity(
                        Intent(
                            this@LoginActivity,
                            HomeActivity::class.java
                        )
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this@LoginActivity,
                        "Correo o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Crear cuenta
        tvCreateAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )

            finish()
        }
    }
}