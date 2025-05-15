package getjson

import tests.main
import java.net.URI
import java.net.http.HttpClient
import kotlin.test.assertEquals
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.Test


class GetJsonTest {
    private val client = HttpClient.newBuilder().build()

    companion object {
        init {
            Thread {
                main()
            }.start()
            Thread.sleep(1000)
        }
    }

    @Test
    fun testIntsEndpoint() {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/ints")).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        println("Status code: ${response.statusCode()}")
        println("Response body: ${response.body()}")

        assertEquals("[1,2,3]", response.body()?.toString())
    }

    @Test
    fun testPairEndpoint() {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/pair")).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals("{\"first\":\"um\",\"second\":\"dois\"}", response.body()?.toString())
    }

    @Test
    fun testPathEndpoint() {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/path/teste")).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals("\"teste!\"", response.body()?.toString())
    }

    @Test
    fun testArgsEndpoint() {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/args?n=3&text=PA")).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals("{\"PA\":\"PAPAPA\"}", response.body()?.toString())
    }
}
