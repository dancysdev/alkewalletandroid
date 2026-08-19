package com.example.alkewallet.model

object MockUserDatabase {

    val usuarios = mutableListOf(

        // =================================================
        // DR. GOKU
        // =================================================

        Usuario(
            id = 1,
            nombre = "Dr. Goku",
            apellido = "Goku",
            correo = "drgoku@ninoguero.com",
            password = "Drgoku2026",
            imagenPerfil = "pic_drgoku",
            alkeNumero = "1001",

            cuenta = Cuenta(
                numero = "1000001",
                saldo = 300000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "American Express",
                    numero = "378765432109876",
                    saldo = 100000.0
                )
            )
        ),


        // =================================================
        // YAMCHA ZEPEDA
        // =================================================

        Usuario(
            id = 2,
            nombre = "Yamcha",
            apellido = "Zepeda",
            correo = "yamcha@ninoguero.com",
            password = "Yamcha2026",
            imagenPerfil = "pic_yamcha",
            alkeNumero = "1002",

            cuenta = Cuenta(
                numero = "1000002",
                saldo = 85000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "Visa",
                    numero = "4111111111111111",
                    saldo = 45000.0
                )
            )
        ),


        // =================================================
        // NAPPA FLORES
        // =================================================

        Usuario(
            id = 3,
            nombre = "Nappa",
            apellido = "Flores",
            correo = "nappa@ninoguero.com",
            password = "Nappa2026",
            imagenPerfil = "pic_nappa",
            alkeNumero = "1003",

            cuenta = Cuenta(
                numero = "1000003",
                saldo = 175000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "Mastercard",
                    numero = "5555555555554444",
                    saldo = 120000.0
                )
            )
        ),


        // =================================================
        // PATANA TOSCANA
        // =================================================

        Usuario(
            id = 4,
            nombre = "Patana",
            apellido = "Toscana",
            correo = "patana@31min.com",
            password = "Patana2026",
            imagenPerfil = "pic_patana",
            alkeNumero = "1004",

            cuenta = Cuenta(
                numero = "1000004",
                saldo = 125000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "Visa",
                    numero = "6011111111111117",
                    saldo = 70000.0
                )
            )
        ),


        // =================================================
        // GUARIPOLO SOPAPI
        // =================================================

        Usuario(
            id = 5,
            nombre = "Guaripolo",
            apellido = "Sopapi",
            correo = "guaripolo@31min.com",
            password = "Guaripolo2026",
            imagenPerfil = "pic_guaripolo",
            alkeNumero = "1005",

            cuenta = Cuenta(
                numero = "1000005",
                saldo = 42000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "Mastercard",
                    numero = "3530111333300000",
                    saldo = 30000.0
                )
            )
        ),


        // =================================================
        // JUAN CARLOS BODOQUE
        // =================================================

        Usuario(
            id = 6,
            nombre = "Juan Carlos",
            apellido = "Bodoque",
            correo = "jcbodoque@31min.com",
            password = "Juan2026",
            imagenPerfil = "pic_jcbodoque",
            alkeNumero = "1006",

            cuenta = Cuenta(
                numero = "1000006",
                saldo = 210000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "American Express",
                    numero = "378282246310005",
                    saldo = 95000.0
                )
            )
        ),


        // =================================================
        // JUANIN HANS HARRY
        // =================================================

        Usuario(
            id = 7,
            nombre = "Juanin",
            apellido = "Hans Harry",
            correo = "juaninhh@31min.com",
            password = "Juanin2026",
            imagenPerfil = "pic_juanin",
            alkeNumero = "1007",

            cuenta = Cuenta(
                numero = "1000007",
                saldo = 67000.0,
                movimientos = mutableListOf()
            ),

            tarjetas = mutableListOf(
                Tarjeta(
                    nombre = "Visa",
                    numero = "4111111111111111",
                    saldo = 55000.0
                )
            )
        )
    )
}