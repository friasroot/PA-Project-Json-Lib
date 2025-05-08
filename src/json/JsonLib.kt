package json

import visitor.JsonVisitor

interface JsonType {
    //val name: String
    val value: Any
    fun replace(objName:String, newValue: JsonType)

    //Valid JSON Types
    val isJsonObject: Boolean get() = false
    val isJsonArray: Boolean get() = false
    val isJsonString: Boolean get() = false
    val isJsonNumber: Boolean get() = false
    val isJsonBoolean: Boolean get() = false
    val isJsonNull: Boolean get() = false

    fun print() {
        var output = "{\n"
        map.onEachIndexed{ index, item ->
            output += """"${item.key}":"""
            output += item.value.toString()

            if(index < map.keys.size - 1)
                output += ",\n"
        }

        output += "\n}"

        println(output)
    }
}

sealed class JsonValue : JsonType {
    abstract fun serialize() : String
    abstract fun accept(visitor: JsonVisitor) : Unit
}

private var map = mutableMapOf<String, JsonType>()

data class JsonObject(val newJsonObject: Map<String, JsonValue>) : JsonValue() {

    override val isJsonObject = true
    override val value = newJsonObject.values

    init {
        newJsonObject.forEach{
            map[it.key] = it.value
        }
    }

    override fun replace(objName: String, newValue: JsonType) {
        if (!newValue.isJsonObject)
            throw Exception("Wrong Type")

        if (!map.containsKey(objName)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[objName] = newValue
    }

    override fun toString(): String {
        var output = "{\n"
        newJsonObject.onEachIndexed{ index, item ->
             output += """"${item.key}":"""
             output += item.value.toString()

             if(index < newJsonObject.size - 1)
                 output += ",\n"
        }
        output += "\n}"
        return output
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitObject(this)
        newJsonObject.values.forEach{ it.accept(visitor) }
    }
}

data class JsonNumber(override val value: Number) : JsonValue()  {
    override val isJsonNumber : Boolean = true

    override fun replace(name:String, newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

        if (!map.containsKey(name)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[name] = newValue
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitNumber(this)
    }
}

data class JsonString(override val value: String) :  JsonValue()  {
    override val isJsonString: Boolean = true

    override fun replace(name: String, newValue: JsonType) {
        if (!newValue.isJsonString)
            throw Exception("Wrong Type")

        if (!map.containsKey(name)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[name] = newValue
    }

    override fun toString(): String {
        return """"$value""""
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitString(this)
    }
}

data class JsonArray(val newArray: List<JsonValue>) :  JsonValue() {
    override val isJsonArray: Boolean = true
    override val value = newArray

    override fun replace(name:String, newValue: JsonType) {
        if (!newValue.isJsonArray)
            throw Exception("Wrong type")

        if (!map.containsKey(name)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[name] = newValue
    }

    override fun toString(): String {
        var text = "["

        value.forEachIndexed{index, vl ->
            text += vl.toString()
            if (index < value.size - 1) {
                text += ","
            }
        }

        return "$text]"
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitArray(this)
        newArray.forEach{it.accept(visitor)}
    }
}

data class JsonBoolean(override val value: Boolean) :  JsonValue() {
    override val isJsonBoolean : Boolean = true

    override fun replace(name:String, newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

        if (!map.containsKey(name)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[name] = newValue
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitBoolean(this)
    }
}
object NULL

data class JsonNull(override val value: Any = NULL) :  JsonValue()  {
    override val isJsonNull : Boolean = true

    //override fun add() { //return value.toString() }

    override fun replace(name:String, newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

        if (!map.containsKey(name)) {
            throw Exception("Entry does not exist, add it instead of replacing")
        }

        map[name] = newValue
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun serialize(): String {
        return this.toString()
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visitNull(this)
    }
}

fun validateIfAllSameJsonType(list: List<JsonValue>) : Boolean {
    val jsonType = list.first()::class

    return list.all { item -> item::class == jsonType }
}

//TODO: Interface com para o tipo de objeto JSON object para os tipos de JSON aceitaveis - Supertipo

//TODO: validador se todos os elementos sao do mesmo tipo
