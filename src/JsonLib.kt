interface JsonType {
    //val name: String
    val value: Any
    fun add()
    //fun get(range: IntRange): String
    //fun insert(text: String, offset: Int)
    fun replace(newValue: JsonType)
    //fun setSelection(selection: IntRange)

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
    //override fun toString(): String
}

private var map = mutableMapOf<String, Any>()

class JsonObject(val newJsonObjects: MutableList<Pair<String, JsonType>>) : JsonType {
    override val isJsonObject = true
    val name = newJsonObjects.first().first
    override val value = newJsonObjects.first().second
    override fun add(){
        //Do nothing
        map[name] = value
    }

    init {
        newJsonObjects.forEach{
            map[it.first] = it.second
        }
    }

    override fun replace(newValue: JsonType) {
        //map[newValue::class] = newValue.value
    }

    override fun toString(): String {
        var output = "{\n"
         newJsonObjects.onEachIndexed{ index, item ->
             output += """"${item.first}":"""
             output += item.second.toString()

             if(index < newJsonObjects.size - 1)
                 output += ",\n"
        }
        output += "\n}"
        return output
    }

    override fun print() {
        return super.print()
    }
}

class JsonNumber(override val value: Number) : JsonType {
    override val isJsonNumber : Boolean = true

    override fun add() {
        //return mutableMapOf(this.name to this.value)
    }

    override fun replace(newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

        //map[newValue.name] = newValue.value
    }

    override fun toString(): String {
        return value.toString()
    }
}

class JsonString(override val value: String) : JsonType {
    override val isJsonString: Boolean = true

    override fun add() {

    }

    override fun replace(newValue: JsonType) {
        if (!newValue.isJsonString)
            throw Exception("Wrong Type")

        //map[newValue.name] = newValue.value
    }

    override fun toString(): String {
        return """"$value""""
    }
}

class JsonArray(newArray: List<JsonType>) : JsonType {
    override val isJsonArray: Boolean = true
    override val value = newArray

    override fun add() {
        // return mutableMapOf("2", 1)
    }

    override fun replace(newValue: JsonType) {
        if (!newValue.isJsonArray)
            throw Exception("Wrong type")

        //map[newValue] = (newValue as JsonArray).value.forEach { v -> v.name to v.value}
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
}

class JsonBoolean(override val value: Boolean) : JsonType {
    override val isJsonBoolean : Boolean = true

    override fun add() {
        //return mutableMapOf(this.name to this.value)
    }

    override fun replace(newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

        //map[newValue.name] = newValue.value
    }

    override fun toString(): String {
        return value.toString()
    }
}

class JsonNull(override val value: Any = "null") : JsonType {
    override val isJsonNull : Boolean = true

    override fun add() {
        //return value.toString()
    }

    override fun replace(newValue: JsonType) {
        if (!newValue.isJsonNumber)
            throw Exception("Wrong Type")

    }

    override fun toString(): String {
        return value.toString()
    }
}

fun validateIfAllSameJsonType(list: List<JsonType>) : Boolean {
    val jsonType = list.first()::class

    return list.all { item -> item::class == jsonType }
}

//TODO: Interface com para o tipo de objeto JSON object para os tipos de JSON aceitaveis - Supertipo

//TODO: validador se todos os elementos sao do mesmo tipo
