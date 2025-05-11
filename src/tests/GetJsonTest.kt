package getjson

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import kotlin.test.assertEquals

class GetJsonTest {
    private val client = OkHttpClient()

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
        val request = Request.Builder().url("http://localhost:8080/api/ints").build()
        client.newCall(request).execute().use { response ->
            assertEquals("[1,2,3]", response.body?.string())
        }
    }

    @Test
    fun testPairEndpoint() {
        val request = Request.Builder().url("http://localhost:8080/api/pair").build()
        client.newCall(request).execute().use { response ->
            assertEquals("{\"first\":\"um\",\"second\":\"dois\"}", response.body?.string())
        }
    }

    @Test
    fun testPathEndpoint() {
        val request = Request.Builder().url("http://localhost:8080/api/path/teste").build()
        client.newCall(request).execute().use { response ->
            assertEquals("\"teste!\"", response.body?.string())
        }
    }

    @Test
    fun testArgsEndpoint() {
        val request = Request.Builder().url("http://localhost:8080/api/args?n=3&text=PA").build()
        client.newCall(request).execute().use { response ->
            assertEquals("{\"PA\":\"PAPAPA\"}", response.body?.string())
        }
    }
}
