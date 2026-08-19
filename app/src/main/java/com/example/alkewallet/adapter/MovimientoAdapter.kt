package com.example.alkewallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.model.FakeDatabase
import com.example.alkewallet.model.Movimiento
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.utils.ImageUtils
import java.text.NumberFormat
import java.util.Locale

class MovimientoAdapter(
    private val movimientos: List<Movimiento>,
    private val usuarioActual: Usuario
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {


    class MovimientoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgUsuario = itemView.findViewById<ImageView>(
            R.id.imgUsuarioMovimiento
        )

        val imgTipo = itemView.findViewById<ImageView>(
            R.id.imgTipoMovimiento
        )

        val tvNombre = itemView.findViewById<TextView>(
            R.id.tvNombreMovimiento
        )

        val tvFecha = itemView.findViewById<TextView>(
            R.id.tvFechaMovimiento
        )

        val tvMonto = itemView.findViewById<TextView>(
            R.id.tvMontoMovimiento
        )
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovimientoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_movimiento,
                parent,
                false
            )

        return MovimientoViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: MovimientoViewHolder,
        position: Int
    ) {

        val movimiento = movimientos[position]

        holder.tvFecha.text = movimiento.fecha


        // =====================================================
        // MONTO
        // =====================================================

        val montoFormateado = NumberFormat
            .getNumberInstance(Locale("es", "CL"))
            .format(movimiento.monto)

        val tipo = movimiento.tipo.lowercase()

        holder.tvMonto.text = when (tipo) {

            "ingreso",
            "transferencia recibida" ->
                "+$${montoFormateado}"

            "retiro",
            "transferencia enviada" ->
                "-$${montoFormateado}"

            else ->
                "$${montoFormateado}"
        }


        // =====================================================
        // TEXTO DEL MOVIMIENTO
        // =====================================================

        val textoOriginal =
            movimiento.usuarioRelacionado
                ?: movimiento.descripcion

        holder.tvNombre.text =
            if (textoOriginal.length > 20) {
                textoOriginal.take(15) + "..."
            } else {
                textoOriginal
            }


        // =====================================================
        // IMAGEN DEL USUARIO
        // =====================================================

        // Por defecto
        holder.imgUsuario.setImageResource(
            R.drawable.user_default
        )


        // -----------------------------------------------------
        // INGRESO DESDE TARJETA
        // -----------------------------------------------------
        // El movimiento pertenece al usuario actual,
        // por lo tanto mostramos su avatar.

        if (tipo == "ingreso") {

            cargarImagenUsuario(
                holder.imgUsuario,
                usuarioActual
            )
        }


        // -----------------------------------------------------
        // TRANSFERENCIAS
        // -----------------------------------------------------
        // En una transferencia buscamos al otro usuario.

        else if (
            tipo == "transferencia recibida" ||
            tipo == "transferencia enviada"
        ) {

            val nombreRelacionado =
                movimiento.usuarioRelacionado

            if (nombreRelacionado != null) {

                val usuarioRelacionado =
                    FakeDatabase.usuarios.find {

                        val nombreCompleto =
                            "${it.nombre} ${it.apellido}"

                        nombreCompleto == nombreRelacionado
                    }

                if (usuarioRelacionado != null) {

                    cargarImagenUsuario(
                        holder.imgUsuario,
                        usuarioRelacionado
                    )
                }
            }
        }


        // =====================================================
        // ICONO DEL TIPO DE MOVIMIENTO
        // =====================================================

        when (tipo) {

            // ic_less_alke contiene visualmente el "+"
            "ingreso",
            "transferencia recibida" -> {

                holder.imgTipo.setImageResource(
                    R.drawable.ic_less_alke
                )
            }

            // ic_plus_alke contiene visualmente el "-"
            "retiro",
            "transferencia enviada" -> {

                holder.imgTipo.setImageResource(
                    R.drawable.ic_plus_alke
                )
            }
        }
    }


    // =========================================================
    // CARGAR IMAGEN DE USUARIO
    // =========================================================

    private fun cargarImagenUsuario(
        imageView: ImageView,
        usuario: Usuario
    ) {

        ImageUtils.cargarImagenPerfil(
            imageView,
            usuario.imagenPerfil
        )
    }


    override fun getItemCount(): Int {
        return movimientos.size
    }
}


