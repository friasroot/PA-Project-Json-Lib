package jsonmodel.visitor

import jsonmodel.JsonArray
import jsonmodel.JsonBoolean
import jsonmodel.JsonNull
import jsonmodel.JsonNumber
import jsonmodel.JsonObject
import jsonmodel.JsonString

interface JsonVisitor {
    fun visitObject(obj: JsonObject)
    fun visitArray(array: JsonArray)
    fun visitString(string: JsonString)
    fun visitNumber(number: JsonNumber)
    fun visitBoolean(boolean: JsonBoolean)
    fun visitNull(nullValue: JsonNull)
}