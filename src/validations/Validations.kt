package validations

import json.JsonArray
import json.JsonBoolean
import json.JsonNull
import json.JsonNumber
import json.JsonObject
import json.JsonString
import visitor.JsonVisitor

class ObjectValidatorVisitor : JsonVisitor {
    var valid = true
    private val objectKeys = mutableListOf<Set<String>>()

    override fun visitObject(obj: JsonObject) {
        val keys = obj.newJsonObject.keys
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
        val type = array.newArray.first()::class
        for(item in array.newArray) {
            if (item::class != type) {
                allSameType = false
                break
            }
        }
    }

    override fun visitObject(obj: JsonObject) {}
    override fun visitString(string: JsonString) {}
    override fun visitNumber(number: JsonNumber) {}
    override fun visitBoolean(boolean: JsonBoolean) {}
    override fun visitNull(nullValue: JsonNull) {}
}