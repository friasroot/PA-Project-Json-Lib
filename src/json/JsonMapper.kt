package json

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class JsonMapper {
    fun toJsonValue(value: Any?): JsonValue = when (value) {
        is JsonValue -> value
        null -> JsonNull()
        is String -> JsonString(value)
        is Number -> JsonNumber(value)
        is Boolean -> JsonBoolean(value)
        is List<*> -> JsonArray(value.map { toJsonValue(it) })
        is Map<*, *> -> JsonObject(
            value.entries.associate {
                val key = it.key as? String
                    ?: throw IllegalArgumentException("Map keys must be strings")
                key to toJsonValue(it.value)
            }.toMutableMap()
        )
        is Enum<*> -> JsonString(value.name)
        else -> {
            val kClass: KClass<out Any> = value::class
            val props = kClass.memberProperties.associate { prop ->
                prop.isAccessible = true
                prop.name to toJsonValue(prop.get(value as Nothing))
            }
            JsonObject(props.toMutableMap())
        }
    }
}