package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.adapter.CardAdapter
import com.example.alkewallet.model.Tarjeta
import com.example.alkewallet.utils.SessionManager

class CardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cards)

        // Usuario actual
        val usuario = SessionManager.usuarioActual

        if (usuario == null) {
            finish()
            return
        }

        // Referencias
        val recyclerCards =
            findViewById<RecyclerView>(R.id.recyclerCards)

        val etCardType =
            findViewById<EditText>(R.id.etCardType)

        val etCardNumber =
            findViewById<EditText>(R.id.etCardNumber)

        val btnAddCard =
            findViewById<Button>(R.id.btnAddCard)

        val btnLessCard =
            findViewById<Button>(R.id.btnLessCard)

        val ivBackArrow =
            findViewById<ImageView>(R.id.ivBackArrow)


        // RecyclerView
        recyclerCards.layoutManager =
            LinearLayoutManager(this)

        val cardAdapter = CardAdapter(
            usuario.tarjetas
        ) { tarjeta ->

            // Seleccionar tarjeta y pasar a Request
            val intent = Intent(
                this,
                RequestActivity::class.java
            )

            intent.putExtra(
                "card_type",
                tarjeta.nombre
            )

            intent.putExtra(
                "card_number",
                tarjeta.numero
            )

            startActivity(intent)
        }

        recyclerCards.adapter = cardAdapter


        // Añadir tarjeta
        btnAddCard.setOnClickListener {

            val tipo =
                etCardType.text.toString().trim()

            val numero =
                etCardNumber.text.toString().trim()

            if (tipo.isEmpty() || numero.isEmpty()) {

                Toast.makeText(
                    this,
                    "Completa los datos de la tarjeta",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val tarjeta = Tarjeta(
                nombre = tipo,
                numero = numero
            )

            usuario.tarjetas.add(tarjeta)

            cardAdapter.notifyItemInserted(
                usuario.tarjetas.lastIndex
            )

            etCardType.text.clear()
            etCardNumber.text.clear()
        }


        // Eliminar tarjeta
        btnLessCard.setOnClickListener {

            val tipo =
                etCardType.text.toString().trim()

            val numero =
                etCardNumber.text.toString().trim()

            val tarjeta = usuario.tarjetas.find {
                it.nombre == tipo &&
                        it.numero == numero
            }

            if (tarjeta == null) {

                Toast.makeText(
                    this,
                    "No se encontró la tarjeta",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val position =
                usuario.tarjetas.indexOf(tarjeta)

            usuario.tarjetas.removeAt(position)

            cardAdapter.notifyItemRemoved(position)

            etCardType.text.clear()
            etCardNumber.text.clear()
        }


        // Volver
        ivBackArrow.setOnClickListener {
            finish()
        }
    }
}