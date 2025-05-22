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
    fun obj(@Param mapString: Map<String, Any>): Map<String, Any> {

        //var mapping = this::class.memberFunctions.filter { it.findAnnotation<Mapping>()!!.path == pathVar }
        //var result = mutableMapOf<String, Any>()

        //result.putAll(mapString.split("=")
        //    .map { it.split(",") }.associate { it.first() to it.last() })

        return mapString //result.toMap()
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
