package com.example.adegacaze.model

data class Address(
	val number: String,
	val city: String,
	val street: String,
	val district: String,
	val name: String,
	val state: String,
	val id: Int,
	val cep: String,
	val complete: String?,
	val pattern: Int
)

data class RespAddress(
	val resp: Address,
	val message: String
)
