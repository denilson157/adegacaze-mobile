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
	val updatedAt: Any,
	val name: String,
	val createdAt: Any,
	val cellphone: Int,
	val emailVerifiedAt: Any,
	val id: Int,
	val isAdmin: Int,
	val email: String
)

