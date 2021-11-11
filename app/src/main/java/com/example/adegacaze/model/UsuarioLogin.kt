package com.example.adegacaze.model

data class UsuarioLogin(
    val resp: Resp,
    val message: String
)

data class Resp(
    val user: User,
    val token: String
)

data class User(
    val birthday: String,
    val name: String,
    val cellphone: Int,
    val id: Int,
    val isAdmin: Boolean,
    val email: String
)

data class RespUser(
    val resp: User,
    val message: String
)