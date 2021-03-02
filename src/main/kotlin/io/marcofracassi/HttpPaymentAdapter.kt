package io.marcofracassi

import daikon.core.HttpStatus.OK_200
import topinambur.Http.Companion.HTTP
import java.lang.RuntimeException

class HttpPaymentAdapter(private val endpoint: String) : PaymentAdapter {

    override fun pay(euro: Int) {
        val response = HTTP.post(url= "$endpoint/", body = """{"amount":$euro}""")
        if(response.statusCode != OK_200)
            throw RuntimeException("Payment failed")
    }
}