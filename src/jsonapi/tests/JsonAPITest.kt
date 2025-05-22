package jsonapi.tests

import jsonmodel.*
import jsonapi.Controller
import jsonapi.JsonAPI
import kotlin.test.assertEquals
import kotlin.test.Test
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Integration test to verify the GET endpoint for JsonAPI that returns a Json object
 *
 * This class tests the Json API calls for the json values
 */
class JsonAPITest {
    private val client = OkHttpClient().newBuilder().build()
    private val url = "http://localhost:8080/api"

    init {
        Thread {
            main()
        }.start()
        Thread.sleep(1000)
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Number.
     *
     * This test uses OkHttp to send a GET request to the `/number` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testNumberEndpoint() {
        val fullUrl = "$url/number?n=2"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonNumber(2)

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON String.
     *
     * This test uses OkHttp to send a GET request to the `/string` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testStringEndpoint() {
        val fullUrl = "$url/string?text=Country"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonString("Country")

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Null.
     *
     * This test uses OkHttp to send a GET request to the `/null` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testNullEndpoint() {
        val fullUrl = "$url/null"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonNull()

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Boolean.
     *
     * This test uses OkHttp to send a GET request to the `/boolean` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testBooleanEndpoint() {
        val fullUrl = "$url/boolean?bool=true"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonBoolean(true)

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Array.
     *
     * This test uses OkHttp to send a GET request to the `/array` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testArrayEndpoint() {
        val fullUrl = "$url/array?list=a&list=b&list=1&list=null"
            //"$url/array?list=a,b,1,null"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonArray(listOf(JsonString("a"), JsonString("b"), JsonNumber(1), JsonNull()))

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON object.
     *
     * This test uses OkHttp to send a GET request to the `/object` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testObjectEndpoint() {
        val fullUrl = "$url/object?ViagemA=\"1\",\"2\",true,null&t1=\"a\"&t2=1"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonObject(mutableMapOf(
            "ViagemA" to JsonArray(mutableListOf(
                JsonString("1"),
                JsonString("2"),
                JsonBoolean(true),
                JsonNull()
            )),
            "t1" to JsonString("a"),
            "t2" to JsonNumber(1)
        ))

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Array for ints.
     *
     * This test uses OkHttp to send a GET request to the `/ints` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testIntsEndpoint() {
        val fullUrl = "$url/ints"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonArray(mutableListOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))

        assertEquals(json.serialize(), response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Object.
     *
     * This test uses OkHttp to send a GET request to the `/pairs` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testPairEndpoint() {
        val fullUrl = "$url/pair"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()

        assertEquals("{\"first\":\"um\", \"second\":\"dois\"}", response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns the path.
     *
     * This test uses OkHttp to send a GET request to the `/path` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testPathEndpoint() {
        val fullUrl = "$url/path/teste"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()

        assertEquals("\"teste!\"", response.body?.string())
    }

    /**
     * Integration test to verifying the GET endpoint that returns a JSON Object.
     *
     * This test uses OkHttp to send a GET request to the `/args` endpoint and
     * parses the response using the custom `JsonValue` model mapper from the JSON framework.
     *
     * The objective is to confirm that the returned JSON matches the expected structure and values.
     */
    @Test
    fun testArgsEndpoint() {
        val fullUrl = "$url/args?n=3&text=PA"
        val request = Request.Builder().url(fullUrl).build()
        val response = client.newCall(request).execute()
        val json = JsonObject(mutableMapOf("PA" to JsonString("PAPAPA")))

        assertEquals(json.serialize(), response.body?.string())
    }
}

private fun main() {
    val app = JsonAPI(Controller::class)
    app.start(8080)
}