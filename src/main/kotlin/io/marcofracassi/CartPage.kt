package io.marcofracassi

import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import daikon.core.RouteAction
import daikon.freemarker.html
import topinambur.Http.Companion.HTTP

class CartPage(private val cartRepository: CartRepository, private val productRepository: ProductRepository) : RouteAction {

    override fun handle(request: Request, response: Response, context: Context) {
        val products = productRepository.all()
        val cartItems = cartRepository.load()

        products.forEach {
            val priceResponse = HTTP.get("https://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new&id=${it.name}")
            val price = priceResponse.body.replace("\n", "").toInt()
            it.withPrice(price)
        }

        response.html(
            "cart", hashMapOf(
                "products" to products,
                "cartItems" to cartItems,
                "total" to cartItems.sumOf { it.price }
            )
        )
    }
}
