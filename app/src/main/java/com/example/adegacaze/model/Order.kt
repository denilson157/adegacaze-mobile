package com.example.adegacaze.model

data class Order(
	val payment_type: String,
	val updated_at: String,
	val user_id: Int,
	val observation: String,
	val adress_id: Int,
	val created_at: String,
	val adress: Address,
	val id: Int,
	val products: List<ProductsItem>
)

data class Pivot(
	val updated_at: String,
	val price: Double,
	val quantity: Int,
	val product_id: Int,
	val created_at: String,
	val orderId: Int
)

data class ProductsItem(
	val old_price: Double,
	val created_at: String,
	val description: String,
	val imgId: String,
	val deleted_at: Any,
	val brandId: Int,
	val updated_at: String,
	val category_id: Int,
	val price: Double,
	val name: String,
	val pivot: Pivot,
	val id: Int,
	val promotion: Boolean
)

data class InsertOrder(
	val payment_type: String,
	val observation: String,
	val adress_id: Int
)

data class RespInsertOrder(
	val resp: Order,
	val message: String
)
