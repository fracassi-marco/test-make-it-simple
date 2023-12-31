package io.marcofracassi

import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import daikon.core.RouteAction


class AddToCartAction(private val cartRepository: io.marcofracassi.CartRepository, private val eventRepository: io.marcofracassi.EventRepository) : RouteAction {

    override fun handle(request: Request, response: Response, context: Context) {
        val price = request.param("price").toInt()
        val name = request.param("name")
        cartRepository.save(io.marcofracassi.CartItem(name, price))

        if(isBigCart())
            eventRepository.send("big_cart_created")

        response.redirect("http://localhost:4545/")
    }

    private fun isBigCart() = cartRepository.load().size > 3
}

