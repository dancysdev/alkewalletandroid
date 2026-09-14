package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.R

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.controller.ApiTestController
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LoginSignupActivity::class.java)
            startActivity(intent)
            finish()

        }, 2000)
        lifecycleScope.launch {
            try {

                val transferencia =
                    ApiTestController(this@SplashActivity)
                        .probarTransferencia(
                            "ALKE121",
                            "ALKE981",
                            10000.0
                        )

                if (transferencia != null) {

                    Log.d(
                        "API_TEST",
                        "Transferencia creada correctamente"
                    )

                    Log.d(
                        "API_TEST",
                        "Emisor: ${transferencia.senderAlkeNumero}"
                    )

                    Log.d(
                        "API_TEST",
                        "Destinatario: ${transferencia.receiverAlkeNumero}"
                    )

                    Log.d(
                        "API_TEST",
                        "Monto: ${transferencia.amount}"
                    )

                } else {

                    Log.d(
                        "API_TEST",
                        "No se pudo crear la transferencia"
                    )
                }

            } catch (e: Exception) {

                Log.e(
                    "API_TEST",
                    "Error creando transferencia",
                    e
                )
            }
        }
    }
}