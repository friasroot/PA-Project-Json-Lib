package getjson

import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpServer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType
import json.toJsonValue // Inferência da fase 2

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param

class GetJson(vararg controllers: KClass<*>) {
    private val routes = mutableMapOf<String, RouteHandler>()

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
        val server = HttpServer.create(InetSocketAddress(port), 0)
        routes.forEach { (path, handler) ->
            server.createContext(path) { exchange ->
                val uri = exchange.requestURI
                val pathParams = extractPathParams(path, uri.path)
                val queryParams = uri.rawQuery?.split("&")?.associate {
                    val (k, v) = it.split("=")
                    k to v
                } ?: emptyMap()

                val result = handler.call(pathParams, queryParams)
                val json = result?.toJsonValue()?.toJsonString() ?: "null"
                exchange.sendResponseHeaders(200, json.toByteArray().size.toLong())
                exchange.responseBody.use { it.write(json.toByteArray()) }
            }
        }
        server.executor = null
        server.start()
        println("Server running on port $port")
    }

    private fun normalize(path: String): String = path.replace(Regex("/+"), "/").removeSuffix("/")

    private fun extractPathParams(routePath: String, actualPath: String): Map<String, String> {
        val routeParts = routePath.split("/")
        val actualParts = actualPath.split("/")
        return routeParts.zip(actualParts).filter { it.first.startsWith("{") && it.first.endsWith("}") }
            .associate { it.first.removeSurrounding("{", "}") to it.second }
    }
}

class RouteHandler(private val instance: Any, private val func: KFunction<*>) {
    fun call(pathParams: Map<String, String>, queryParams: Map<String, String>): Any? {
        val args = func.parameters.map { param ->
            when {
                param.kind == KParameter.Kind.INSTANCE -> instance
                param.findAnnotation<Path>() != null -> pathParams[param.name]?.convert(param.type.javaType)
                param.findAnnotation<Param>() != null -> queryParams[param.name]?.convert(param.type.javaType)
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
}
