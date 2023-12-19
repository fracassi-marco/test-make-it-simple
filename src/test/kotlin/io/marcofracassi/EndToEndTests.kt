package io.marcofracassi

import daikon.HttpServer
import daikon.core.HttpStatus.OK_200
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.className
import org.openqa.selenium.By.id
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

class EndToEndTests {

    private var app = App()
    private val awsEndpoint = "http://localhost:4566"
    private val connection = DbConnection("localhost:5432")
    private val sqsMessage = SqsMessage(awsEndpoint, "events_queue")
    private val browser = ChromeDriver()

    @BeforeEach
    fun beforeEach() {
        app.start()
        sqsMessage.empty()
        DbCartRepository(connection).empty()
        DbProductRepository(connection).save(
            Product(
                name = "Potato",
                description = null,
                image = "https://s3.amazonaws.com/pix.iemoji.com/images/emoji/apple/ios-12/256/potato.png",
                availableQuantity = null,
                bookedQuantity = null
            )
        )
    }

    @AfterEach
    fun afterEach() {
        browser.close()
        app.close()
        // remove product: potato
        connection.create().use {
            it.prepareStatement("DELETE FROM products WHERE name = '${"Potato"}'").execute()
        }
    }

    @Test
    fun payByCash() {
        // open home page and click on Potato
        browser.get("http://localhost:4545/")
        browser.findElement(id("button-Potato")).click()

        // assert that potato is into the cart
        assertThat(browser.findElements(className("cart-item")).single().text).isEqualTo("Potato")

        browser.findElement(id("button-cash")).click()
        // assert that the cart is empty
        assertThat(browser.findElements(className("cart-item"))).isEmpty()
    }

    @Test
    fun payByExternalSystem() {
        // stab external payment system
        HttpServer(4546) {
            post("/") { _, res, _ -> res.status(OK_200) }
        }.start().use {
            // open home page and click on Potato
            browser.get("http://localhost:4545/")
            browser.findElement(id("button-Potato")).click()

            // assert that potato is into the cart
            assertThat(browser.findElements(className("cart-item")).single().text).isEqualTo("Potato")

            browser.findElement(id("button-external")).click()
            // assert that the cart is empty
            assertThat(browser.findElements(className("cart-item"))).isEmpty()

            // assert that the bill exists on S3
            S3Object(awsEndpoint, "bills", "bill_1.txt").assertExists()
        }
    }

    @Test
    fun bigCart() {
        // open home page and click on Potato 4 times
        browser.get("http://localhost:4545/")
        browser.findElement(id("button-Potato")).click()
        browser.findElement(id("button-Potato")).click()
        browser.findElement(id("button-Potato")).click()
        browser.findElement(id("button-Potato")).click()

        // assert that event exists in the queue
        SqsMessage(awsEndpoint, "events_queue").assertExists("big_cart_created")
    }
}