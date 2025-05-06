//
//interface JsonType {
//    val name: String
//    val value: Any
//    fun add() : MutableMap<String, Any>
//    //fun get(range: IntRange): String
//    //fun insert(text: String, offset: Int)
//    fun replace(newValue: JsonType)
//    //fun setSelection(selection: IntRange)
//
//    //Valid JSON Types
//    val isJsonObject: Boolean get() = false
//    val isJsonArray: Boolean get() = false
//    val isJsonString: Boolean get() = false
//    val isJsonNumber: Boolean get() = false
//    val isJsonBoolean: Boolean get() = false
//    val isJsonNull: Boolean get() = false
//
//    fun print() = println(map)
//    //override fun toString(): String
//}
//
//private var map = mutableMapOf<String, Any>()
//
//class JsonObject(newJsonObject: Pair<String, JsonType>) : JsonType {
//    override val isJsonObject = true
//    override val name = newJsonObject.first
//    override val value = newJsonObject.second
//    override fun add(): MutableMap<String, Any> {
//        //Do nothing
//        return mutableMapOf(this.name to this.value)
//    }
//
//    init {
//        map[name] = value.add()
//    }
//
//    override fun replace(newValue: JsonType) {
//        map[newValue.name] = newValue.value
//    }
//
//    override fun print() {
//        println(map)
//    }
//}
//
//class JsonNumber(override val name: String, override val value: Number) : JsonType {
//    override val isJsonNumber : Boolean = true
//
//    override fun add(): MutableMap<String, Any> {
//        return mutableMapOf(this.name to this.value)
//    }
//
//    override fun replace(newValue: JsonType) {
//        if (!newValue.isJsonNumber)
//            throw Exception("Wrong Type")
//
//        map[newValue.name] = newValue.value
//    }
//}
//
//class JsonString(override val name: String, override val value: String) : JsonType {
//    override val isJsonString: Boolean = true
//
//    override fun add(): MutableMap<String, Any> {
//        return mutableMapOf(this.name to this.value)
//    }
//
//    override fun replace(newValue: JsonType) {
//        if (!newValue.isJsonString)
//            throw Exception("Wrong Type")
//
//        map[newValue.name] = newValue.value
//    }
//}
//
//class JsonArray(newArray: Pair<String, List<JsonType>>) : JsonType {
//    override val isJsonArray: Boolean = true
//    override val name = newArray.first
//    override val value = newArray.second
//
//    override fun add() : MutableMap<String, Any> {
//        return mutableMapOf(this.name to this.value.forEach{it.add()})
//    }
//
//    override fun replace(newValue: JsonType) {
//        if (!newValue.isJsonArray)
//            throw Exception("Wrong type")
//
//        map[newValue.name] = (newValue as JsonArray).value.forEach { v -> v.name to v.value}
//    }
//}
//
//
////TODO: Interface com para o tipo de objeto JSON object para os tipos de JSON aceitaveis - Supertipo
//
////TODO: validador se todos os elementos sao do mesmo tipo