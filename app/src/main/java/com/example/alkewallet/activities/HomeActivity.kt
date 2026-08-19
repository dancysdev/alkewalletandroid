package com.example.alkewallet.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.adapter.MovimientoAdapter
import com.example.alkewallet.utils.SessionManager

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        // =================================================
        // USUARIO ACTUAL
        // =================================================

        val usuario = SessionManager.usuarioActual


        // =================================================
        // REFERENCIAS
        // =================================================

        val btnEnviarDinero =
            findViewById<Button>(R.id.btnEnviarDinero)

        val btnIngresarDinero =
            findViewById<Button>(R.id.btnIngresarDinero)

        val imgPerfil =
            findViewById<ImageView>(R.id.imgPerfil)

        val tvSaludo =
            findViewById<TextView>(R.id.tvSaludo)

        val tvBalance =
            findViewById<TextView>(R.id.tvBalance)

        val recyclerMovimientos =
            findViewById<RecyclerView>(
                R.id.recyclerMovimientos
            )

        val imgEmpty =
            findViewById<ImageView>(R.id.imgEmpty)

        val tvWhitOutTransfer =
            findViewById<TextView>(
                R.id.tvWhitOutTransfer
            )


        // =================================================
        // MOSTRAR INFORMACIÓN DEL USUARIO
        // =================================================

        if (usuario != null) {

            tvSaludo.text =
                "Hola, ${usuario.nombre}"

            tvBalance.text =
                "$${usuario.cuenta.saldo}"


            // =================================================
            // IMAGEN DE PERFIL
            // =================================================

            if (usuario.imagenPerfil == "default_profile") {

                imgPerfil.setImageResource(
                    R.drawable.user_default
                )

            } else {

                try {

                    imgPerfil.setImageURI(
                        Uri.parse(usuario.imagenPerfil)
                    )

                } catch (e: Exception) {

                    imgPerfil.setImageResource(
                        R.drawable.user_default
                    )
                }
            }


            // =================================================
            // MOVIMIENTOS
            // =================================================

            recyclerMovimientos.layoutManager =
                LinearLayoutManager(this)

            recyclerMovimientos.adapter =
                MovimientoAdapter(
                    usuario.cuenta.movimientos,
                    usuario
                )


            // =================================================
            // ESTADO DE LOS MOVIMIENTOS
            // =================================================

            if (usuario.cuenta.movimientos.isEmpty()) {

                recyclerMovimientos.visibility =
                    View.GONE

                imgEmpty.visibility =
                    View.VISIBLE

                tvWhitOutTransfer.visibility =
                    View.VISIBLE

            } else {

                recyclerMovimientos.visibility =
                    View.VISIBLE

                imgEmpty.visibility =
                    View.GONE

                tvWhitOutTransfer.visibility =
                    View.GONE
            }
        }


        // =================================================
        // ENVIAR DINERO → CONTACTOS
        // =================================================

        btnEnviarDinero.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ContactsActivity::class.java
                )
            )
        }


        // =================================================
        // INGRESAR DINERO → TARJETAS
        // =================================================

        btnIngresarDinero.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CardsActivity::class.java
                )
            )
        }


        // =================================================
        // PERFIL
        // =================================================

        imgPerfil.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }
    }
}

