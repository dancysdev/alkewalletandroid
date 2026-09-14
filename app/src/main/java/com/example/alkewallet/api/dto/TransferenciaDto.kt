package com.example.alkewallet.api.dto

data class TransferenciaDto(
    val senderId: String,
    val receiverId: String,
    val senderAlkeNumero: String,
    val receiverAlkeNumero: String,
    val amount: Double,
    val type: String
)