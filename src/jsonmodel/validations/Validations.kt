package jsonmodel.validations

import jsonmodel.JsonArray
import jsonmodel.JsonBoolean
import jsonmodel.JsonNull
import jsonmodel.JsonNumber
import jsonmodel.JsonObject
import jsonmodel.JsonString
import jsonmodel.visitor.JsonVisitor

/**
 * A visitor validator
 *
 * This class uses visitors, to facilitate the development of new features
 * that involve traversing the structure recursively.
 *
 * Extends JsonVisitor interface
 */
class ObjectValidatorVisitor : JsonVisitor {
    var valid = true
    private val objectKeys = mutableListOf<Set<String>>()

    override fun visitObject(obj: JsonObject) {
        val keys = obj.entries.keys
        if (keys.size != keys.toSet().size) valid = false
        objectKeys.add(keys)
    }

    override fun visitArray(array: JsonArray) {}
    override fun visitString(string: JsonString) {}
    override fun visitNumber(number: JsonNumber) {}
    override fun visitBoolean(boolean: JsonBoolean) {}
    override fun visitNull(nullValue: JsonNull) {}
}

class ArrayTypeCheckerVisitor : JsonVisitor {
    var allSameType = true

    override fun visitArray(array: JsonArray) {
        val type = array.elements.filterNot { item -> item is JsonNull}.first()::class
        allSameType = array.elements.all{ it::class ==  type}
    }

    override fun visitObject(obj: JsonObject) {}
    override fun visitString(string: JsonString) {}
    override fun visitNumber(number: JsonNumber) {}
    override fun visitBoolean(boolean: JsonBoolean) {}
    override fun visitNull(nullValue: JsonNull) {}
}