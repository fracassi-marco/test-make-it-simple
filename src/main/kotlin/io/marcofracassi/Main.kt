package io.marcofracassi

import daikon.HttpServer

fun main() {
    App().start()
}

class App : AutoCloseable {

    private val server = HttpServer {
        val dbConnection = DbConnection("localhost:5432")
        val cartRepository = DbCartRepository(dbConnection)
        val productRepository = DbProductRepository(dbConnection)
        val billRepository = S3BillRepository("http://localhost:4566").init()
        val eventRepository = SqsEventRepository("http://localhost:4566").init()
        val paymentAdapter = HttpPaymentAdapter("http://localhost:4546")

        get("/", CartPage(cartRepository, productRepository))
        post("/add-to-cart", io.marcofracassi.AddToCartAction(cartRepository, eventRepository))
        post("/pay-by-cash", PayByCashAction(cartRepository, billRepository))
        post("/pay-external", PayExternalAction(paymentAdapter, cartRepository, billRepository))
    }

    fun start() {
        server.start()
    }

    override fun close() {
        server.close()
    }
}