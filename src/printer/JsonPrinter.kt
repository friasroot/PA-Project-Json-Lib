package printer

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor


class JsonPrinter : Printer{

    fun serialize(obj : Any) : String {
        return formatValues(obj)
    }

    override fun print(text: String) {

        println("\n$text\n")
        println(text.replace("[", " [\n")
                    .replace(",", ",\n")
                    .replace("]", "\n]")
                    .replace(":", ": ")
                    .replace("{", "{\n")
        )
    }

    private fun formatValues(obj: Any, text: String = "") : String {
        var newText = text
        val clazz = obj::class

        if (!clazz.isData) {
           return mapValue(obj)
        }

        val params = clazz.primaryConstructor?.parameters

        newText = newText.plus(obj::class.simpleName + "[")

        params?.forEachIndexed { index, v ->
            val prop = clazz.matchProperty(v)
            val value = prop.call(obj)

            newText = newText.plus(""""${prop.name}":""")

            if (value != null && value::class.isData)
                newText = formatValues(value, newText)
            else {
                val list = if(value is List<*>) value else listOf(value)

                newText = newText.plus(formatJson(prop.returnType.classifier, list))
            }

            if (index != params.size - 1)
                newText = newText.plus(",")
            else
                newText = newText.plus("]")
        }

        return newText
    }

    private fun KClass<*>.matchProperty(parameter: KParameter) : KProperty<*> {
        require(isData)
        return declaredMemberProperties.first { it.name == parameter.name }
    }

    private fun mapValue(obj: Any?): String =
        if (obj is String)
            """"$obj""""
        else
            obj.toString()

    private fun formatJson(obj: KClassifier?, values: List<*>): String {
        if (obj == kotlin.collections.List::class) {
            var text = "{"

            values.forEachIndexed { index, value ->
                text = text.plus(mapValue(value))
                if (index != values.size - 1)
                    text = text.plus(", ")
            }

            return text.plus("}")
        } else {
            return mapValue(values.first())
        }
    }
}
