package jsonapi

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import jsonmodel.JsonMapper
import kotlin.reflect.KClass
import kotlin.reflect.full.*

@Target(AnnotationTarget.CLASS)
annotation class RestController

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param

/**
 * Json API class that initialized a server that can be used to create a Json Object
 * using the Controller mappings
 *
 * @param controllers with the list of possible api mapping and their handlers
 */
class JsonAPI(vararg controllers: KClass<*>) {
    private val routes = mutableMapOf<String, RouteHandler>()
    private lateinit var server: HttpServer

    init {
        controllers.forEach { controllerClass ->
            val basePath = controllerClass.findAnnotation<Mapping>()?.path ?: ""
            val instance = controllerClass.createInstance()

            controllerClass.memberFunctions.filter { it.findAnnotation<Mapping>() != null }.forEach { func ->
                val pathVars = func.parameters.filter { it.hasAnnotation<Path>() }.map{ "\\{${it.name}\\}" }
                val subPath = func.findAnnotation<Mapping>()!!.path
                var publicSubPath = subPath
                pathVars.forEach {
                    publicSubPath = publicSubPath.replace("/$it".toRegex(), "")
                }
                val publicFullPath = normalize("/$basePath/$publicSubPath")
                val fullPath = normalize("/$basePath/$subPath")
                //Needs to have a public fullPath and a private fullPath to handle calls that include path parameters
                routes[publicFullPath] = RouteHandler(instance, func, fullPath)
            }
        }
    }

    fun start(port: Int) {
        server = HttpServer.create(InetSocketAddress(port), 0)
        routes.forEach { (path, handler) ->
            server.createContext(path) { exchange ->
                try {
                    if (exchange.requestMethod != "GET") {
                        exchange.sendResponseHeaders(405, 0)
                        exchange.responseBody.close()
                        return@createContext
                    }

                    val uri = exchange.requestURI
                    //Uses privatePath instead of the url path in the routes for calls with path params
                    val pathParams = extractPathParams(handler.privatePath, uri.path)
                    val queryParams = parseQueryParams(uri.rawQuery)

                    val result = handler.call(pathParams, queryParams)
                    val json = JsonMapper().toJsonValue(result)
                    val bytes = json.serialize().toByteArray()
                    exchange.sendResponseHeaders(200, bytes.size.toLong())
                    exchange.responseBody.use { it.write(bytes) }
                } catch (ex: Exception) {
                    val errorMsg = ex.message ?: "Internal error"
                    exchange.sendResponseHeaders(500, errorMsg.toByteArray().size.toLong())
                    exchange.responseBody.use { it.write(errorMsg.toByteArray()) }
                }
            }
        }
        server.executor = null
        server.start()
        println("Server running on port $port")
    }

    fun stop() {
        server.stop(0)
    }

    private fun normalize(path: String): String = path.replace(Regex("/+"), "/").removeSuffix("/")

    private fun extractPathParams(routePath: String, actualPath: String): Map<String, String> {
        val routeParts = routePath.split("/")
        val actualParts = actualPath.split("/")
        return routeParts.zip(actualParts).filter { it.first.startsWith("{") && it.first.endsWith("}") }
            .associate { it.first.removeSurrounding("{", "}") to it.second }
    }

    // Parses query parameters like ?list=1&list=2
    private fun parseQueryParams(query: String?): Map<String, List<String>> {
        if (query == null)
            return emptyMap()

        return query.split("&")
            .map {
                val parts = it.split("=", limit = 2)
                parts[0] to parts[1]
            }
            .groupBy({ it.first }, { it.second })
    }
}


