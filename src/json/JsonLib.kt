package json

import visitor.JsonVisitor

sealed class JsonValue {
    abstract fun serialize() : String
    abstract fun accept(visitor: JsonVisitor)
}

data class JsonObject(val newJsonObject: MutableMap<String, JsonValue>) : JsonValue() {

    fun replace(objName: String, newValue: JsonValue) {

        if (!newJsonObject.containsKey(objName)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        newJsonObject[objName] = newValue
    }

    override fun serialize(): String {
        return newJsonObject.entries.joinToString(prefix = "{", postfix = "}") { (name, type) ->
            "\"${name}\":${type.serialize()}"
        }
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitObject(this)
        newJsonObject.values.forEach{ it.accept(visitor) }
    }
}

data class JsonArray(val elements: List<JsonValue>) :  JsonValue() {

    override fun serialize(): String {
        return elements.joinToString(prefix = "[", postfix = "]") { it.serialize() }
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitArray(this)
        elements.forEach{it.accept(visitor)}
    }
}

data class JsonNumber(val value: Number) : JsonValue()  {

    override fun serialize(): String {
        return value.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitNumber(this)
    }
}

data class JsonString(val value: String) :  JsonValue()  {

    override fun serialize(): String {
        return "\"${value.replace("\"", "\\\"")}\""
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitString(this)
    }
}

data class JsonBoolean(val value: Boolean) :  JsonValue() {

    override fun serialize(): String {
        return value.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitBoolean(this)
    }
}
object NULL

data class JsonNull(val value: Any = NULL) :  JsonValue()  {

    override fun serialize(): String {
        return "null"
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitNull(this)
    }
}
