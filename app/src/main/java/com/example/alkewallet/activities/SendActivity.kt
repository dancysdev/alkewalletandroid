package com.example.alkewallet.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.R
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.utils.SessionManager
import kotlinx.coroutines.launch

class SendActivity : AppCompatActivity() {

    private val userController by lazy {
        UserController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        // =====================================================
        // Usuario actual
        // =====================================================

        val usuarioActual =
            SessionManager.usuarioActual

        if (usuarioActual == null) {
            finish()
            return
        }

        // =====================================================
        // Referencias
        // =====================================================

        val ivBackArrow =
            findViewById<ImageView>(R.id.ivBackArrow)

        val imgUsuario =
            findViewById<ImageView>(R.id.imgCardSelected)

        val tvNombre =
            findViewById<TextView>(R.id.tvCardName)

        val tvCorreo =
            findViewById<TextView>(R.id.tvCardNum)

        val etMonto =
            findViewById<EditText>(R.id.etMountOutput)

        val etNota =
            findViewById<EditText>(R.id.etTransferNote)

        val btnSend =
            findViewById<Button>(R.id.btnSend)

        // =====================================================
        // Recibir ALKE del contacto
        // =====================================================

        val alkeNumeroContacto =
            intent.getStringExtra("alkeNumeroContacto")

        if (alkeNumeroContacto.isNullOrBlank()) {

            Toast.makeText(
                this,
                "No se encontró el contacto",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        // =====================================================
        // Buscar destinatario en Room
        // =====================================================

        lifecycleScope.launch {

            val destinatario =
                userController.buscarUsuarioPorAlke(
                    alkeNumeroContacto
                )

            if (destinatario == null) {

                Toast.makeText(
                    this@SendActivity,
                    "No se encontró el contacto",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
                return@launch
            }

            // =================================================
            // Mostrar información del destinatario
            // =================================================

            tvNombre.text =
                "${destinatario.nombre} ${destinatario.apellido}"

            tvCorreo.text =
                destinatario.correo

            // =================================================
            // Mostrar avatar del destinatario
            // =================================================

            val imagenPerfil =
                destinatario.imagenPerfil

            if (!imagenPerfil.isNullOrEmpty()) {

                imgUsuario.setImageURI(
                    Uri.parse(imagenPerfil)
                )

            } else {

                imgUsuario.setImageResource(
                    R.drawable.user_default
                )
            }

            // =================================================
            // Regresar a contactos
            // =================================================

            ivBackArrow.setOnClickListener {
                finish()
            }

            // =================================================
            // Enviar dinero
            // =================================================

            btnSend.setOnClickListener {

                val montoTexto =
                    etMonto.text
                        .toString()
                        .trim()

                if (montoTexto.isEmpty()) {

                    Toast.makeText(
                        this@SendActivity,
                        "Introduce un monto",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                val monto =
                    montoTexto.toDoubleOrNull()

                if (monto == null || monto <= 0) {

                    Toast.makeText(
                        this@SendActivity,
                        "Introduce un monto válido",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                // =============================================
                // Nota opcional
                // =============================================

                val nota =
                    etNota.text
                        .toString()
                        .trim()

                // =============================================
                // Realizar transferencia mediante Room
                // =============================================

                lifecycleScope.launch {

                    val transferenciaRealizada =
                        userController.transferirDinero(
                            emisor = usuarioActual,
                            destinatario = destinatario,
                            monto = monto,
                            nota = nota.ifBlank { null }
                        )

                    // =========================================
                    // Resultado
                    // =========================================

                    if (transferenciaRealizada) {

                        Toast.makeText(
                            this@SendActivity,
                            "Transferencia realizada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this@SendActivity,
                                HomeActivity::class.java
                            )
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this@SendActivity,
                            "Saldo insuficiente para realizar la transferencia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}



