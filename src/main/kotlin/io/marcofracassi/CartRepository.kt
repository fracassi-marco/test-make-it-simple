package io.marcofracassi

interface CartRepository {
    fun empty()
    fun save(cartItem: CartItem)
    fun load(): List<CartItem>
}