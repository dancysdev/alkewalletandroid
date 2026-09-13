package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.adapter.CardAdapter
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.model.Tarjeta
import com.example.alkewallet.utils.SessionManager
import com.example.alkewallet.utils.Validator
import kotlinx.coroutines.launch

class CardsActivity : AppCompatActivity() {

    private val userController by lazy {
        UserController(this)
    }

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


        // =================================================
        // FORMATEAR NÚMERO DE TARJETA
        // =================================================

        etCardNumber.addTextChangedListener(object : TextWatcher {

            private var editando = false

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (editando) {
                    return
                }

                editando = true

                val numeroLimpio =
                    s.toString().replace(" ", "")

                val numeroFormateado =
                    numeroLimpio
                        .chunked(4)
                        .joinToString(" ")

                if (numeroFormateado != s.toString()) {

                    etCardNumber.setText(
                        numeroFormateado
                    )

                    etCardNumber.setSelection(
                        numeroFormateado.length
                    )
                }

                editando = false
            }
        })


        // =================================================
        // RECYCLERVIEW
        // =================================================

        recyclerCards.layoutManager =
            LinearLayoutManager(this)

        val cardAdapter = CardAdapter(
            usuario.tarjetas
        ) { tarjeta ->

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


        // =================================================
        // AÑADIR TARJETA
        // =================================================

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

            // Validar tipo de tarjeta
            val errorTipo =
                Validator.validarTipoTarjeta(tipo)

            if (errorTipo != null) {

                Toast.makeText(
                    this,
                    errorTipo,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Validar número de tarjeta
            val errorNumero =
                Validator.validarNumeroTarjeta(numero)

            if (errorNumero != null) {

                Toast.makeText(
                    this,
                    errorNumero,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Normalizar datos
            val tipoNormalizado =
                tipo.uppercase()

            val numeroLimpio =
                numero.replace(" ", "")

            val tarjeta = Tarjeta(
                nombre = tipoNormalizado,
                numero = numeroLimpio
            )

            lifecycleScope.launch {

                // Guardar primero en Room
                val guardada =
                    userController.agregarTarjeta(
                        usuario = usuario,
                        tarjeta = tarjeta
                    )

                if (guardada) {

                    // Solo después de guardar correctamente
                    // sincronizamos la sesión en memoria.
                    usuario.tarjetas.add(tarjeta)

                    cardAdapter.notifyItemInserted(
                        usuario.tarjetas.lastIndex
                    )

                    etCardType.text.clear()
                    etCardNumber.text.clear()

                    Toast.makeText(
                        this@CardsActivity,
                        "Tarjeta agregada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this@CardsActivity,
                        "La tarjeta ya existe",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        // =================================================
        // ELIMINAR TARJETA
        // =================================================

        btnLessCard.setOnClickListener {

            val tipo =
                etCardType.text.toString().trim()

            val numero =
                etCardNumber.text.toString().trim()

            val numeroLimpio =
                numero.replace(" ", "")

            val tarjeta = usuario.tarjetas.find {
                it.nombre.equals(
                    tipo,
                    ignoreCase = true
                ) &&
                        it.numero == numeroLimpio
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

            lifecycleScope.launch {

                // Eliminar primero de Room
                val eliminada =
                    userController.eliminarTarjeta(
                        usuario = usuario,
                        tarjeta = tarjeta
                    )

                if (eliminada) {

                    // Solo después de eliminar correctamente
                    // sincronizamos la sesión en memoria.
                    usuario.tarjetas.removeAt(position)

                    cardAdapter.notifyItemRemoved(
                        position
                    )

                    etCardType.text.clear()
                    etCardNumber.text.clear()

                    Toast.makeText(
                        this@CardsActivity,
                        "Tarjeta eliminada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this@CardsActivity,
                        "No se pudo eliminar la tarjeta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        // =================================================
        // VOLVER
        // =================================================

        ivBackArrow.setOnClickListener {
            finish()
        }
    }
}
