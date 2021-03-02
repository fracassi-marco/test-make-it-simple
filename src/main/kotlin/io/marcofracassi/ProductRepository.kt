package io.marcofracassi

interface ProductRepository {
    fun all(): List<Product>
    fun save(product: Product)
}
