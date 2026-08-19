package com.example.alkewallet.utils

object Validator {

    fun validarPassword(password: String): String? {

        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres"
        }

        if (!password.any { it.isUpperCase() }) {
            return "La contraseña debe contener al menos una mayúscula"
        }

        if (!password.any { it.isDigit() }) {
            return "La contraseña debe contener al menos un número"
        }

        return null
    }

    fun validarEmail(email: String): String? {

        val patron = Regex("^[^@\\s]+@[^@\\s]+\\.com$")

        if (!patron.matches(email)) {
            return "Ingresa un correo válido (ejemplo: usuario@correo.com)"
        }

        return null
    }

    fun validarTipoTarjeta(tipo: String): String? {

        val tiposValidos = listOf(
            "VISA",
            "MASTERCARD",
            "AMERICAN EXPRESS",
            "DISCOVER",
            "JCB",
            "UNIONPAY",
            "DINERS CLUB"
        )

        if (tipo.uppercase() !in tiposValidos) {
            return "Tipo de tarjeta no válido"
        }

        return null
    }

    fun validarNumeroTarjeta(numero: String): String? {

        // Eliminar espacios para validar el número real
        val numeroLimpio = numero.replace(" ", "")

        // Debe contener únicamente números
        if (!numeroLimpio.all { it.isDigit() }) {
            return "El número de tarjeta solo debe contener números"
        }

        // Debe tener exactamente 16 dígitos
        if (numeroLimpio.length != 16) {
            return "El número de tarjeta debe tener 16 dígitos"
        }

        // Algoritmo de Luhn
        var suma = 0
        var duplicar = false

        for (i in numeroLimpio.length - 1 downTo 0) {

            var digito =
                numeroLimpio[i].digitToInt()

            if (duplicar) {
                digito *= 2

                if (digito > 9) {
                    digito -= 9
                }
            }

            suma += digito
            duplicar = !duplicar
        }

        if (suma % 10 != 0) {
            return "El número de tarjeta no es válido"
        }

        return null
    }
}