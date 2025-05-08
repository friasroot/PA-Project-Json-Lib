package tests

import json.JsonArray
import json.JsonNumber
import json.JsonString
import validations.ArrayTypeCheckerVisitor

annotation class TestCase
class JsonLibTest {

    @TestCase
    fun testArrayValidationWithAllSameJsonTypes(x: Any): Unit {
        val jsonArray = JsonArray(listOf(JsonString("dois"), JsonString("um")))
        val typeChecker = ArrayTypeCheckerVisitor()
        jsonArray.accept(typeChecker)

        kotlin.test.assertEquals(true, typeChecker.allSameType)
    }

    @TestCase
    fun testArrayValidationWithDiffJsonTypes(x: Any): Unit {
        val jsonArray = JsonArray(listOf(JsonString("dois"), JsonNumber(1)))
        val typeChecker = ArrayTypeCheckerVisitor()
        jsonArray.accept(typeChecker)

        kotlin.test.assertEquals(false, typeChecker.allSameType)
    }
}
//TODO: Comparar que o objecto final é o esperado em vez de comparar strings/valores