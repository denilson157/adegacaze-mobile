package com.example.adegacaze.model


data class AddCart(
    val product_id: Int,
    val quantity_add: Int
)


data class RemoveCart(
    val product_id: Int,
    val quantity_remove: Int
)


data class AddCartResp(
    val resp: Resp,
    val message: String
)

data class Cart(
    val product: Product,
    val quantity: Int,
    val userId: Int,
    val product_id: Int,
    val id: Int
)