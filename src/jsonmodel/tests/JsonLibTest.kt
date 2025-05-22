package jsonmodel.tests

import jsonmodel.*
import jsonmodel.validations.ArrayTypeCheckerVisitor
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonLibTest {

    @Test
    fun testArrayValidationWithAllSameJsonTypes() {
        val jsonArray = JsonArray(listOf(JsonString("dois"), JsonString("um")))
        val typeChecker = ArrayTypeCheckerVisitor()
        jsonArray.accept(typeChecker)

        assertEquals(true, typeChecker.allSameType)
    }

    @Test
    fun testArrayValidationWithDiffJsonTypes() {
        val jsonArray = JsonArray(listOf(JsonString("dois"), JsonNumber(1)))
        val typeChecker = ArrayTypeCheckerVisitor()
        jsonArray.accept(typeChecker)

        assertEquals(false, typeChecker.allSameType)
    }

    //TODO: Comparar que o objecto final é o esperado em vez de comparar strings/valores
    @Test
    fun testFinalObject() {
        val jsonObject = JsonObject(mutableMapOf(
            "ViagemA" to JsonArray(mutableListOf(
                JsonString("1"),
                JsonBoolean(true),
                JsonNull()
            )),
            "t1" to JsonString("a"),
            "t2" to JsonNumber(1)
        ))

        println(jsonObject)
        val startsWithJsonObject = jsonObject::class == JsonObject::class
        val containsJsonArray = jsonObject.entries.containsValue(JsonArray(mutableListOf(
            JsonString("1"),
            JsonBoolean(true),
            JsonNull()
        )))
        val containsJsonString = jsonObject.entries.containsValue(JsonString("a"))
        val containsJsonNumber = jsonObject.entries.containsValue(JsonNumber(1))

        assertEquals(true,
            startsWithJsonObject && containsJsonArray && containsJsonString && containsJsonNumber)
    }
}