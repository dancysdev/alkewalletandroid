package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.R
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.utils.SessionManager

class RequestActivity : AppCompatActivity() {

    private val userController = UserController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        // Usuario actual
        val usuario = SessionManager.usuarioActual

        if (usuario == null) {
            finish()
            return
        }

        // Referencias
        val ivBackArrow =
            findViewById<ImageView>(R.id.ivBackArrow)

        val tvCardName =
            findViewById<TextView>(R.id.tvCardName)

        val tvCardNum =
            findViewById<TextView>(R.id.tvCardNum)

        val etMountOutput =
            findViewById<EditText>(R.id.etMountOutput)

        val etTransferNote =
            findViewById<EditText>(R.id.etTransferNote)

        val btnRequest =
            findViewById<Button>(R.id.btnRequest)


        // Datos de la tarjeta seleccionada
        val cardType =
            intent.getStringExtra("card_type")

        val cardNumber =
            intent.getStringExtra("card_number")


        // Buscar tarjeta original del usuario
        val tarjeta = usuario.tarjetas.find {
            it.nombre == cardType &&
                    it.numero == cardNumber
        }


        // Si no existe la tarjeta, volver
        if (tarjeta == null) {

            Toast.makeText(
                this,
                "No se encontró la tarjeta",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }


        // Mostrar tarjeta seleccionada
        tvCardName.text = tarjeta.nombre
        tvCardNum.text = tarjeta.numero


        // Regresar al Home
        ivBackArrow.setOnClickListener {

            startActivity(
                Intent(this, HomeActivity::class.java)
            )

            finish()
        }


        // Ingresar dinero
        btnRequest.setOnClickListener {

            val montoTexto =
                etMountOutput.text.toString().trim()

            val nota =
                etTransferNote.text.toString().trim()


            if (montoTexto.isEmpty()) {

                Toast.makeText(
                    this,
                    "Ingresa un monto",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val monto =
                montoTexto.toDoubleOrNull()


            if (monto == null || monto <= 0) {

                Toast.makeText(
                    this,
                    "Ingresa un monto válido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Verificar saldo disponible de la tarjeta
            if (monto > tarjeta.saldo) {

                Toast.makeText(
                    this,
                    "Saldo insuficiente en la tarjeta",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Descontar dinero de la tarjeta
            tarjeta.saldo -= monto


            // Ingresar dinero a la wallet
            val ingresoRealizado =
                userController.ingresarDinero(
                    usuario = usuario,
                    monto = monto,
                    tarjeta = tarjeta,
                    nota = nota.ifBlank { null }
                )


            if (ingresoRealizado) {

                Toast.makeText(
                    this,
                    "Dinero ingresado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, HomeActivity::class.java)
                )

                finish()

            } else {

                // Si la operación falla,
                // devolver el saldo a la tarjeta
                tarjeta.saldo += monto

                Toast.makeText(
                    this,
                    "No fue posible realizar el ingreso",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

