package visitor

import json.JsonArray
import json.JsonBoolean
import json.JsonNull
import json.JsonNumber
import json.JsonObject
import json.JsonString

interface JsonVisitor {
    fun visitObject(obj: JsonObject)
    fun visitArray(array: JsonArray)
    fun visitString(string: JsonString)
    fun visitNumber(number: JsonNumber)
    fun visitBoolean(boolean: JsonBoolean)
    fun visitNull(nullValue: JsonNull)
}