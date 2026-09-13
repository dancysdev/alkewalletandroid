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
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.model.Cuenta
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.utils.Validator
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private val userController by lazy {
        UserController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Referencias
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPasswordMake = findViewById<EditText>(R.id.etPasswordMake)
        val etPasswordConfirm = findViewById<EditText>(R.id.etPasswordConfirm)

        val btnMakeAccount = findViewById<Button>(R.id.btnMakeAccount)
        val tvYaTieneCuenta = findViewById<TextView>(R.id.tvYaTieneCuenta)

        // Crear cuenta → Login
        btnMakeAccount.setOnClickListener {

            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val correo = etEmail.text.toString()
            val password = etPasswordMake.text.toString()
            val confirmPassword = etPasswordConfirm.text.toString()

            // Validar correo
            val errorEmail = Validator.validarEmail(correo)

            if (errorEmail != null) {

                Toast.makeText(
                    this,
                    errorEmail,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Validar contraseña
            val errorPassword = Validator.validarPassword(password)

            if (errorPassword != null) {

                Toast.makeText(
                    this,
                    errorPassword,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Confirmar contraseña
            if (password != confirmPassword) {

                Toast.makeText(
                    this,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                // Número de cuenta temporal.
                // Más adelante reemplazaremos esta generación
                // por una estrategia definitiva.
                val numeroCuenta =
                    "CTA${System.currentTimeMillis()}"

                val cuenta = Cuenta(
                    numero = numeroCuenta,
                    saldo = 0.0,
                    movimientos = mutableListOf()
                )

                val usuario = Usuario(
                    id = 0,
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    password = password,

                    // Sin imagen personalizada todavía.
                    // La interfaz mostrará user_default.
                    imagenPerfil = null,

                    // UserController generará el ALKE aleatorio.
                    alkeNumero = "",

                    cuenta = cuenta
                )

                userController.crearUsuario(usuario)

                Toast.makeText(
                    this@SignUpActivity,
                    "Cuenta creada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this@SignUpActivity,
                        LoginActivity::class.java
                    )
                )

                finish()
            }
        }

        // Ya tiene cuenta → Login
        tvYaTieneCuenta.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
            finish()
        }
    }
}