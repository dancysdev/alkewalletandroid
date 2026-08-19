package com.example.alkewallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.model.Tarjeta

class CardAdapter(
    private val tarjetas: MutableList<Tarjeta>,
    private val onTarjetaClick: (Tarjeta) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgCardType = itemView.findViewById<ImageView>(
            R.id.imgCardType
        )

        val tvNombreCard = itemView.findViewById<TextView>(
            R.id.tvNombreCard
        )

        val tvNumeroCard = itemView.findViewById<TextView>(
            R.id.tvNumeroCard
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_card,
                parent,
                false
            )

        return CardViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CardViewHolder,
        position: Int
    ) {

        val tarjeta = tarjetas[position]

        holder.tvNombreCard.text =
            tarjeta.nombre

        holder.tvNumeroCard.text =
            tarjeta.numero

        holder.imgCardType.setImageResource(
            R.drawable.ic_card
        )

        holder.itemView.setOnClickListener {
            onTarjetaClick(tarjeta)
        }
    }

    override fun getItemCount(): Int {
        return tarjetas.size
    }

    fun actualizarTarjetas(
        nuevasTarjetas: List<Tarjeta>
    ) {

        tarjetas.clear()
        tarjetas.addAll(nuevasTarjetas)

        notifyDataSetChanged()
    }
}