package com.example.adegacaze.model

data class Product(
	val updatedAt: String,
	val categoryId: Int,
	val price: String,
	val oldPrice: String,
	val name: String,
	val createdAt: String,
	val description: String,
	val img_id: String,
	val id: Int,
	val deletedAt: Any,
	val promotion: String,
	val brandId: Int
)

