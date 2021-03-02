package io.marcofracassi

class DbProductRepository(private val connection: DbConnection) : ProductRepository {

    override fun all(): List<Product> {
        val results = connection.create().use {
            it.prepareStatement("SELECT * FROM products").executeQuery()
        }

        val items = mutableListOf<Product>()
        while (results.next()) {
            items.add(Product(
                results.getString("name"),
                results.getString("description"),
                results.getString("image"),
                results.getInt("available_quantity"),
                results.getInt("booked_quantity")
            ))
        }

        return items
    }

    override fun save(product: Product) {
        product.validate()

        connection.create().use {
            it.prepareStatement("INSERT INTO products VALUES (?, ?, ?, ?, ?)",).apply {
                setString(1, product.name)
                setString(2, product.description)
                setString(3, product.image)
                setInt(4, product.availableQuantity!!)
                setInt(5, product.bookedQuantity!!)
            }.execute()
        }
    }
}


