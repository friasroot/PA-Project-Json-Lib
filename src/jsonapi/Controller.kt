package jsonapi

@RestController
@Mapping("api")
class Controller {
    @Mapping("number")
    fun number(@Param n: Int): Number = n

    @Mapping("string")
    fun str(@Param text: String): String = text

    @Mapping("array")
    fun array(@Param list: List<*>): List<*> = list

    @Mapping("object")
    fun obj(@Param mapString: List<*>): Map<String, Any> {
        val outputMap = mutableMapOf<String, Any>()
        mapString.map { elem ->
            if (elem is String) {
                val parts = elem.split("=", limit = 2)
                val values = parts[1].split(",")
                outputMap[parts[0]] = ( if (values.count() <= 1)
                                            values.first().convert()
                                        else
                                            values.map { it.convert() }
                                        ) as Any
            }
        }
        return outputMap
    }

    @Mapping("null")
    fun nullValue() = null

    @Mapping("boolean")
    fun bool(@Param bool: Boolean) = bool

    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("pair")
    fun obj(): Pair<String, String> = "um" to "dois"

    @Mapping("path/{pathvar}")
    fun path(@Path pathvar: String): String = "$pathvar!"

    @Mapping("args")
    fun args(@Param n: Int, @Param text: String): Map<String, String> = mapOf(text to text.repeat(n))
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
