package jsonapi

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import jsonmodel.JsonMapper
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType

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
                val subPath = func.findAnnotation<Mapping>()!!.path
                val fullPath = normalize("/$basePath/$subPath")
                routes[fullPath] = RouteHandler(instance, func)
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
                    val pathParams = extractPathParams(path, uri.path)
                    val queryParams = if(uri.rawQuery != null) parseQueryParams(uri.rawQuery) else emptyMap()

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
}

/**
 * Handles the routing for the api calls
 *
 * @param instance created for the route
 * @param func api function
 */
class RouteHandler(private val instance: Any, private val func: KFunction<*>) {
    fun call(pathParams: Map<String, String>, queryParams: Map<String, List<String>>): Any? {
        val args = func.parameters.map { param ->
            when {
                param.kind == KParameter.Kind.INSTANCE -> instance
                param.findAnnotation<Path>() != null -> pathParams[param.name]?.convert(param.type.javaType)
                param.findAnnotation<Param>() != null -> {
                    val paramValue = queryParams[param.name]
                    if (paramValue is List && paramValue.count() > 1)
                        paramValue?.map { it.convert() }
                    else if (paramValue is List && paramValue.count() == 1)
                        paramValue[0].convert(param.type.javaType)
                    else
                        return paramValue
                }
                else -> null
            }
        }
        return func.call(*args.toTypedArray())
    }

    private fun String.convert(type: java.lang.reflect.Type): Any = when (type.typeName) {
        "int", "java.lang.Integer", "kotlin.Int" -> this.toInt()
        "double", "java.lang.Double", "kotlin.Double" -> this.toDouble()
        "boolean", "java.lang.Boolean", "kotlin.Boolean" -> this.toBoolean()
        else -> this
    }

    private fun String.convert(): Any? {
        if (this == "null")
            return null
        if (this.toIntOrNull() != null)
            return this.toInt()
        else if (this.toDoubleOrNull() != null)
            return this.toDouble()
        else if (this.toLongOrNull() != null)
            return this.toLong()
        else if (this.toFloatOrNull() != null)
            return this.toFloat()
        else if (this.toBooleanStrictOrNull() != null)
            return this.toBoolean()

        return this
    }
}

// Parses query parameters like ?list=1&list=2
private fun parseQueryParams(query: String): Map<String, List<String>> {
    return query.split("&")
        .map {
            val parts = it.split("=", limit = 2)
            parts[0] to parts[1]
        }
        .groupBy({ it.first }, { it.second })
}

