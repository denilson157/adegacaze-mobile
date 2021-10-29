package com.example.adegacaze.model

data class Order(
	val paymentType: String,
	val updatedAt: String,
	val userId: Int,
	val observation: String,
	val adressId: Int,
	val createdAt: String,
	val adress: Address,
	val id: Int,
	val products: List<ProductsItem>
)

data class Pivot(
	val updatedAt: String,
	val price: Double,
	val quantity: Int,
	val productId: Int,
	val createdAt: String,
	val orderId: Int
)

data class ProductsItem(
	val oldPrice: Double,
	val createdAt: String,
	val description: String,
	val imgId: String,
	val deletedAt: Any,
	val brandId: Int,
	val updatedAt: String,
	val categoryId: Int,
	val price: Double,
	val name: String,
	val pivot: Pivot,
	val id: Int,
	val promotion: Boolean
)