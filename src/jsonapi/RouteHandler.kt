package jsonapi

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaType

/**
 * Handles the routing for the api calls
 *
 */
class RouteHandler(private val instance: Any, private val func: KFunction<*>, val privatePath: String) {
    fun call(pathParams: Map<String, String>, queryParams: Map<String, List<String>>): Any? {
        val args = func.parameters.map { param ->
            when {
                param.kind == KParameter.Kind.INSTANCE -> instance
                param.findAnnotation<Path>() != null -> pathParams[param.name]?.convert(param.type.javaType)
                param.findAnnotation<Param>() != null -> {
                    val paramValue = queryParams[param.name]
                    if (paramValue is List && paramValue.count() > 1)
                        paramValue.map { it.convert() }
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
}

private fun String.convert(): Any? {
    if (this == "null")
        return null

    return  this.toIntOrNull() ?:
            this.toDoubleOrNull() ?:
            this.toLongOrNull() ?:
            this.toFloatOrNull() ?:
            this.toBooleanStrictOrNull() ?:
            this
}
